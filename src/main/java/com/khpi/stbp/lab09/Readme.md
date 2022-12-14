# Лабораторна №9

## Захист від зміни бінарного файлу

---

### Мета: Навчитися підписувати виконувані файли.

### Завдання:

- Створити сертифікат
- Проінсталювати його в систему, щоб він був "довіреним"
- Використовуючи проект будь-якої попередньої роботи, виконати підпис виконуваного файлу
за допомогою утиліти JarSigner
- Виконати верифікацію підпису:
  - чи є підписаний сертифікат валідним
  - чи не було зміни файлу та його код цілісний

### Хід роботи

Для початку перевіряється чи не підписаний ***.jar*** файл з самого початку


![](doc/1.png)


Як видно на фото відповідь: ні

Тоді генерується ключ наступною командою. Заповнюються персональні дані, та записується ключ у файл ***.jks***

![](doc/2.png)


![](doc/3.png)

Далі створюється сертифікат ***.cer*** на основі згенерованого файлу ***.jks*** наступною командою.

![](doc/4.png)


![](doc/5.png)


Залишається підписати ***.jar*** файл створеним сертифікатом ***.cer*** наступною командою.


![](doc/6.png)


Створюється новий окремий ***.jar*** який є підписаний. Залишається перевірити його.

![](doc/7.png)


Як видно на фото, ***.jar*** сертифікований.