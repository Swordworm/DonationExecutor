package igorlink.service;

import com.destroystokyo.paper.Title;
import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static Boolean isPluginActive;

    //Вывод сообщения в консоль
    public static void logToConsole(String text){
        Bukkit.getConsoleSender().sendMessage("§c[DonationExecutor] §f" + text);
    }

    //Отправка сообщения игроку со стороны плагина
    public static void sendSysMsgToPlayer(Player player, String text){
        player.sendMessage("§c[DE] §f" + text);
    }

    public static byte[] decodeUsingBigInteger(String hexString) {
        byte[] byteArray = new BigInteger(hexString, 16).toByteArray();
        if (byteArray[0] == 0) {
            byte[] output = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, output,0, output.length);
            return output;
        }
        return byteArray;
    }

    public static Boolean CheckNameAndToken() {
        isPluginActive = true;
        if ((DonationExecutor.getInstance().getConfig().getString("DonationAlertsToken") == "") || (DonationExecutor.getInstance().getConfig().getString("DonationAlertsToken") == null)) {
            logToConsole("Вы не указали свой токен DonationAlerts в файле конфигурации плагина, поэтому сейчас плагин не работает.");
            isPluginActive = false;
        } else if ((DonationExecutor.getInstance().getConfig().getString("Nickname") == "") || (DonationExecutor.getInstance().getConfig().getString("Nickname") == "")) {
            logToConsole("Вы не указали свой игровой никнейм в файле конфигурации плагина, поэтому сейчас плагин не работает.");
            isPluginActive = false;
        }
        return isPluginActive;
    }

    public static void announce(String donaterName, String subText, String alterSubtext, Player player, Boolean bigAnnounce) {
        String _donaterName = donaterName;

        if  (bigAnnounce) {
            if (donaterName.equals("")) {
                _donaterName = "Кто-то";
            }
            Title title = new Title("§c" + _donaterName, "§f" + subText);
            player.sendTitle(title);
            player.sendMessage("§c[DE] §fДонатер §c" + _donaterName, "§f" + subText);
        }

        if (_donaterName == "") {
            _donaterName = "Кто-то";
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!(p.getName() == player.getName())) {
                p.sendMessage("§c[DE] §fДонатер §c" + _donaterName + " §f" + alterSubtext + " §b" + player.getName());
            }
        }


    }

    public static Vector genVec(Location a, Location b, Boolean isNormalized) {
        Vector vec = locToVec(b).clone().subtract(locToVec(a));
        if (isNormalized) {
            return vec.normalize();
        } else {
            return vec;
        }
    }

    public static Vector genVec(Location a, Location b) {
        Vector vec = locToVec(b).clone().subtract(locToVec(a));
            return vec.normalize();
    }

    public static Vector locToVec(Location loc) {
        Vector vec = new Vector(loc.getX(), loc.getY(), loc.getZ());
        return vec;
    }

    public static String cutOffKopeykis(String donationAmountWithKopeykis) {
        String amountWithoutKopeykis = "";
        for (int i = 0; i <= donationAmountWithKopeykis.length() - 1; i++) {
            if (donationAmountWithKopeykis.charAt(i) == '.') {
                break;
            } else if (donationAmountWithKopeykis.charAt(i) == ' ') {
                continue;
            } else {
                amountWithoutKopeykis = amountWithoutKopeykis + donationAmountWithKopeykis.charAt(i);
            }
        }

        return amountWithoutKopeykis;
    }

    public static Boolean isBlackListed(String text) {
        HashMap<Character, List<Character>> mapOfSynonimousChars = new HashMap<Character, List<Character>>();

        mapOfSynonimousChars.put('h', (Arrays.asList('x', 'х', 'н', 'n'))); //eng
        mapOfSynonimousChars.put('n', (Arrays.asList('н', 'й', 'и'))); //eng
        mapOfSynonimousChars.put('н', (Arrays.asList('h', 'n', 'й', 'и'))); //rus
        mapOfSynonimousChars.put('e', (Arrays.asList('е', '3', 'з'))); //eng
        mapOfSynonimousChars.put('е', (Arrays.asList('e', '3', 'з'))); //rus
        mapOfSynonimousChars.put('г', (Arrays.asList('r', 'я', 'g', '7'))); //rus
        mapOfSynonimousChars.put('r', (Arrays.asList('г', 'я', 'g', '7'))); //eng
        mapOfSynonimousChars.put('g', (Arrays.asList('г', 'r', '7'))); //eng
        mapOfSynonimousChars.put('p', (Arrays.asList('п', 'р', 'n', 'я', 'r'))); //eng
        mapOfSynonimousChars.put('р', (Arrays.asList('p', 'r', 'я'))); //rus
        mapOfSynonimousChars.put('п', (Arrays.asList('p', 'n', 'и', 'р'))); //rus
        mapOfSynonimousChars.put('o', (Arrays.asList('о', '0'))); //eng
        mapOfSynonimousChars.put('о', (Arrays.asList('o', '0'))); //rus
        mapOfSynonimousChars.put('a', (Arrays.asList('а'))); //eng
        mapOfSynonimousChars.put('а', (Arrays.asList('a'))); //rus
        mapOfSynonimousChars.put('и', (Arrays.asList('i', 'n', 'e', 'е', '|', 'l', '!', '1', '3', 'й'))); //rus
        mapOfSynonimousChars.put('i', (Arrays.asList('1', 'и', 'e', 'е', '|', 'l', '!', 'й'))); //eng
        mapOfSynonimousChars.put('с', (Arrays.asList('c', 's', '$', '5'))); //rus
        mapOfSynonimousChars.put('s', (Arrays.asList('c', 'с', '$', '5'))); //eng
        mapOfSynonimousChars.put('c', (Arrays.asList('s', 'с', '$', '5'))); //eng
        mapOfSynonimousChars.put('л', (Arrays.asList('l', '1', '|'))); //rus
        mapOfSynonimousChars.put('l', (Arrays.asList('л', '1', '|', '!'))); //eng
        mapOfSynonimousChars.put('1', (Arrays.asList('л', 'i', 'l', '|'))); //eng
        mapOfSynonimousChars.put('d', (Arrays.asList('д', 'л'))); //eng
        mapOfSynonimousChars.put('д', (Arrays.asList('d', 'л', '9'))); //rus
        mapOfSynonimousChars.put('y', (Arrays.asList('у', 'u', 'ы'))); //eng
        mapOfSynonimousChars.put('у', (Arrays.asList('y', 'u', 'ы'))); //rus
        mapOfSynonimousChars.put('x', (Arrays.asList('х', 'h'))); //eng
        mapOfSynonimousChars.put('х', (Arrays.asList('x', 'h'))); //rus
        mapOfSynonimousChars.put('ы', (Arrays.asList('у', 'u', 'y'))); //rus
        mapOfSynonimousChars.put('ы', (Arrays.asList('у', 'u', 'y'))); //rus
        mapOfSynonimousChars.put('ч', (Arrays.asList('4')));//rus
        mapOfSynonimousChars.put('k', (Arrays.asList('к')));//eng
        mapOfSynonimousChars.put('к', (Arrays.asList('k')));//rus
        mapOfSynonimousChars.put('0', (Arrays.asList('o', 'о'))); //num
        mapOfSynonimousChars.put('1', (Arrays.asList('i', 'l'))); //num
        mapOfSynonimousChars.put('3', (Arrays.asList('e', 'е','з')));
        mapOfSynonimousChars.put('4', (Arrays.asList('ч')));
        mapOfSynonimousChars.put('5', (Arrays.asList('с', 'c', 's')));
        mapOfSynonimousChars.put('9', (Arrays.asList('r', 'я')));


        String validationText = text.toLowerCase();

        Pattern pattern = Pattern.compile("[l1i]*[\\-]*[l1i]*");
        Matcher matcher = pattern.matcher(validationText);
        if ( (matcher.find()) && (matcher.group().length()>0) ) {
           validationText = validationText.replace(matcher.group(), "н");
        }

        validationText = validationText.replace(" ", "");
        validationText = validationText.replace(",", "");
        validationText = validationText.replace(".", "");
        validationText = validationText.replace("-", "");
        validationText = validationText.replace("%", "");
        validationText = validationText.replace("*", "");
        validationText = validationText.replace("?", "");

        if (validationText.length() == 0) {
            return false;
        }

//        for (String ss : MainConfig.listOfWhiteListedSubstrings) {
//            if (validationText.contains(ss)) {
//                validationText = validationText.replace(ss, "");
//            }
//        }


        if (!(validationText.matches("[a-zа-я0-9$!ё]*"))) {
            return true;
        }


        for (String ss : MainConfig.listOfBlackListedSubstrings) {
            for (int i = 0; i <= validationText.length() - ss.length(); i++) {
                int tempi = i;
                for (int j = 0; j <= ss.length(); j++) {

                    if (j == ss.length()) {
                        return true;
                    }

                    if (validationText.charAt(tempi + j) == ss.charAt(j)) {
                        continue;
                    } else if ((mapOfSynonimousChars.containsKey(ss.charAt(j)))) {
                        if ((mapOfSynonimousChars.get(ss.charAt(j)).contains(validationText.charAt(tempi + j)))) {
                            continue;
                        }
                    }

                    Boolean repeated = true;
                    Boolean finishCycle = false;
                    while ((repeated) && (!finishCycle)) {
                        if (j==0) {
                            break;
                        }
                        if (!(validationText.charAt(tempi + j) == validationText.charAt(tempi + j - 1))) {
                            if (!(mapOfSynonimousChars.containsKey(validationText.charAt(tempi + j)))) {
                                repeated = false;
                                break;
                            } else if (!(mapOfSynonimousChars.get(validationText.charAt(tempi + j)).contains(validationText.charAt(tempi + j - 1)))) {
                                repeated = false;
                                break;
                            }
                        }
                        tempi++;
                        if ((validationText.length()-tempi-j) < (ss.length()-j)) {
                            finishCycle=true;
                            break;
                        }
                    }

                    if (finishCycle) {
                        break;
                    }

                    if (validationText.charAt(tempi + j) == ss.charAt(j)) {
                        continue;
                    } else if ((mapOfSynonimousChars.containsKey(ss.charAt(j)))) {
                        if ((mapOfSynonimousChars.get(ss.charAt(j)).contains(validationText.charAt(tempi + j)))) {
                            continue;
                        }
                    }

                    break;

                }
            }
        }

        return false;
    }


}

