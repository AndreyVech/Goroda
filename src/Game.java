import java.util.Scanner;
import java.util.*;
import java.util.Arrays;

public class Game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FindCity rules = new FindCity(); //покажем правила игры
        int step = 0; //создадим счетчик ходов для проверки использованных городов и чего-нибудь еще.
        System.out.print("Введите количество допустимых ошибок: ");
        int maxErrors = Integer.parseInt(scanner.nextLine());
        int error = 0;
        while (true) {
            String userWord = scanner.nextLine();
            FindCity user = new FindCity(userWord);
            FindCity check = new FindCity(userWord);
            if (Objects.equals(userWord, "0")) {
                break; //правило выхода из игры
            } else if (step == 0) { //на первом ходе не проверяем повторные города
                if (Arrays.asList(CityData.city).contains(user.userWord)) { //проверим что ввел пользователь, это город?
                    FindCity comp = new FindCity(userWord);
                    comp.compAnswer(userWord); //ответ компьютера
                    step++; //добавим счетчик ходов
                } else {
                    error ++;
                    System.out.println("Нет такого города");
                }
            } else if (step >= 1) { //на последующих ходах проверим повторы
                if (check.checkUser(userWord)) { //проверяем ответ пользователя
                    if (Arrays.asList(CityData.city).contains(user.userWord)) { //проверим что ввел пользователь, это город?
                        if (!user.checkInOldUser()) { //если пользователь не повторился
                            FindCity comp = new FindCity(userWord);
                            comp.compAnswer(userWord); //ответ компьютера
                        } else {
                            error ++;
                            System.out.println("Было!");
                            if (error == maxErrors) {
                                System.out.println("Ты проиграл на " + step + " ходе!");
                                break;
                            }
                        }
                    } else {
                        error ++;
                        System.out.println("Нет такого города");
                        if (error == maxErrors) {
                            System.out.println("Ты проиграл на " + step + " ходе!");
                            break;
                        }
                    }
                } else {
                    error ++;
                    System.out.println("Неправильно!");
                    if (error == maxErrors) {
                        System.out.println("Ты проиграл на " + step + " ходе!");
                        break;
                    }
                }
            }
        }
    }
}

class FindCity {
    String userWord;
    String compWord;
    int t;
    boolean inOld = false;
    private String rules;

    //меню игры
    public FindCity() {
        this.rules = "Введите город для старта \nвведите '0' для выхода";
        System.out.println(this.rules);
    }

    //приведем ответ пользователя к нужному формату
    public FindCity(String userWord) {
        String[] words = userWord.toLowerCase().replaceAll("-", " ").trim().split(" ");
        String result = "";
        for (int i = 0; i < words.length; i++) {
            String str = words[i];
            char firstChar = str.charAt(0);
            if (!Character.isUpperCase(firstChar)) {
                result += Character.toUpperCase(firstChar) + str.substring(1);
            } else {
                result += str + "";
            }
        }
        this.userWord = result;
    }

    //проверяем ответ пользователя
    public boolean checkUser(String userWord) {
        String oldCompWord = CityData.oldCity.get(0); //проверим последний ответ компьютера
        int pos = oldCompWord.length() - 1;
        char lastChar = oldCompWord.toUpperCase().charAt(pos);
        if (lastChar == 'Й') {
            lastChar = 'И';
        } else if (lastChar == 'Ь' || lastChar == 'Ы' || lastChar == 'Ъ') {
            lastChar = oldCompWord.toUpperCase().charAt(pos - 1);
        }
        String lastComp = Character.toString(lastChar); //найдем последнюю букву в последнем ответе ПК
        char firstChar = userWord.toUpperCase().charAt(0);
        String firstUser = Character.toString(firstChar);
        return lastComp.equals(firstUser); //проверим совпадение букв в ответах ПК и пользователя
    }

    //готовим ответ ПК
    public void compAnswer(String userWord) {
        CityData.oldCity.add(t, this.userWord); //ответ пользователя запишем в массив использованных
        int pos = userWord.length() - 1;
        char lastChar = userWord.toUpperCase().charAt(pos);
        String last = Character.toString(lastChar); //выберем последнюю букву в верхнем регистре из ответа пользователя
        System.out.print("Мне на " + last + " : ");
        for (int x = 0; x < CityData.city.length; x++) { //пройдемся по базе городов
            int n = (int) Math.floor(Math.random() * CityData.city.length);
            String cityAtData = CityData.city[n]; //выберем случайный город

            //проверяем совпадение последней буквы ответа пользователя и ПК
            if (Character.toString(cityAtData.toUpperCase().charAt(0)).equals(last)
                    //проверяем повторы
                    && !Arrays.asList(CityData.oldCity).contains(cityAtData)) {
                this.compWord = cityAtData; //запишем ответ
            } else {
                x++;
            }
        }
        CityData.oldCity.add(t, this.compWord); //запишем ответ в базу использованных
        System.out.println(compWord);
    }

    //проверяем пользователя на повторы
    public boolean checkInOldUser() {
        return CityData.oldCity.contains(this.userWord);
    }
}
