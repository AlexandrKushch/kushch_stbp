package com.khpi.stbp.lab03.src.controller;

import com.khpi.stbp.lab03.src.model.EncryptedUserMessages;
import com.khpi.stbp.lab03.src.model.UserMessages;
import com.khpi.stbp.lab03.src.model.Users;
import com.khpi.stbp.lab03.src.rsa.Key;
import com.khpi.stbp.lab03.src.rsa.RSA;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    private EncryptedUserMessages messages = new EncryptedUserMessages();
    private Users users = new Users();
    private UserMessages userMessages = new UserMessages();

    @GetMapping
    public String firstEnter() {
        Key key = new Key();
        String id = Integer.toString(key.getN());

        users.addUser(id, key);
        messages.addUser(id);
        userMessages.addUser(id);
        return "redirect:/" + id;
    }

    @GetMapping("/{id}")
    public String getMessages(
            @PathVariable String id,
            Model model) {
        Key key = users.getUsers().get(id);
        Map<String, List<List<BigInteger>>> allMessages = messages.getAllMessages();
        userMessages.clear();

        for (String k :
                allMessages.keySet()) {
            for (List<BigInteger> eMessage:
                 allMessages.get(k)) {
                BigInteger[] eMes = new BigInteger[eMessage.size()];
                eMessage.toArray(eMes);

                if (eMes.length != 0 && key != null) {
                    BigInteger[] dMes = RSA.decryption(eMes, key);
                    String s = "";

                    for (BigInteger b :
                            dMes) {
                        s += (char) b.intValue();
                    }

                    userMessages.addMessage(k, s);
                }
            }

            if (userMessages.getAllMessages().get(k).size() == 0) {
                userMessages.addMessage(k, "");
            }
        }

        model.addAttribute("id", id);
        model.addAttribute("usersMessages", userMessages.getAllMessages());
        return "index";
    }

    @GetMapping("/{id}/friendPage/{uKey}")
    public String getMyPage(@PathVariable String id, @PathVariable String uKey, Model model) {
        model.addAttribute("user", users.getUsers().get(uKey));
        model.addAttribute("id", id);
        return "friendPage";
    }

    @PostMapping("/{id}")
    public String addMessage(@PathVariable String id,
                             @RequestParam String message,
                             @RequestParam int e,
                             @RequestParam int n,
                             Model model) {
        BigInteger[] encryptedMessage = RSA.encryption(message, new Key(e, n));
        messages.addMessage(id, encryptedMessage);

        return "redirect:/" + id;
    }
}
