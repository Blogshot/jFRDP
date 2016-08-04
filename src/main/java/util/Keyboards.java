package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Keyboards extends ArrayList {

  private static ArrayList<Keyboard> keyboards;

  public static ArrayList<Keyboard> getKeyboards() {

    if (keyboards == null) {
      Type type = new TypeToken<ArrayList<Keyboard>>() {
      }.getType();

      keyboards = new Gson().fromJson(jsonString, type);
    }

    return keyboards;
  }

  public static int indexOf(String code) {
    for (Keyboard keyboard : keyboards) {
      if (keyboard.code.equals(code)) {
        return keyboards.indexOf(keyboard);
      }
    }

    // default to Latin American
    return 68;
  }


  public static class Keyboard {
    String name;
    public String code;

    @Override
    public String toString() {
      return name;
    }
  }

  private static String jsonString = "[\n"+
      "  {\n"+
      "    \"code\": \"0x00000401\",\n"+
      "    \"name\": \"Arabic (101)\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000402\",\n"+
      "    \"name\": \"Bulgarian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000404\",\n"+
      "    \"name\": \"Chinese (Traditional) - US Keyboard\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000405\",\n"+
      "    \"name\": \"Czech\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000406\",\n"+
      "    \"name\": \"Danish\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000407\",\n"+
      "    \"name\": \"German\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000408\",\n"+
      "    \"name\": \"Greek\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000409\",\n"+
      "    \"name\": \"US\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000040A\",\n"+
      "    \"name\": \"Spanish\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000040B\",\n"+
      "    \"name\": \"Finnish\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000040C\",\n"+
      "    \"name\": \"French\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000040D\",\n"+
      "    \"name\": \"Hebrew\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000040E\",\n"+
      "    \"name\": \"Hungarian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000040F\",\n"+
      "    \"name\": \"Icelandic\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000410\",\n"+
      "    \"name\": \"Italian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000411\",\n"+
      "    \"name\": \"Japanese\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000412\",\n"+
      "    \"name\": \"Korean\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000413\",\n"+
      "    \"name\": \"Dutch\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000414\",\n"+
      "    \"name\": \"Norwegian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000415\",\n"+
      "    \"name\": \"Polish (Programmers)\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000416\",\n"+
      "    \"name\": \"Portuguese (Brazilian ABNT)\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000418\",\n"+
      "    \"name\": \"Romanian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000419\",\n"+
      "    \"name\": \"Russian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000041A\",\n"+
      "    \"name\": \"Croatian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000041B\",\n"+
      "    \"name\": \"Slovak\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000041C\",\n"+
      "    \"name\": \"Albanian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000041D\",\n"+
      "    \"name\": \"Swedish\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000041E\",\n"+
      "    \"name\": \"Thai Kedmanee\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000041F\",\n"+
      "    \"name\": \"Turkish Q\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000420\",\n"+
      "    \"name\": \"Urdu\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000422\",\n"+
      "    \"name\": \"Ukrainian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000423\",\n"+
      "    \"name\": \"Belarusian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000424\",\n"+
      "    \"name\": \"Slovenian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000425\",\n"+
      "    \"name\": \"Estonian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000426\",\n"+
      "    \"name\": \"Latvian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000427\",\n"+
      "    \"name\": \"Lithuanian IBM\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000429\",\n"+
      "    \"name\": \"Farsi\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000042A\",\n"+
      "    \"name\": \"Vietnamese\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000042B\",\n"+
      "    \"name\": \"Armenian Eastern\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000042C\",\n"+
      "    \"name\": \"Azeri Latin\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000042F\",\n"+
      "    \"name\": \"FYRO Macedonian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000437\",\n"+
      "    \"name\": \"Georgian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000438\",\n"+
      "    \"name\": \"Faeroese\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000439\",\n"+
      "    \"name\": \"Devanagari - INSCRIPT\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000043A\",\n"+
      "    \"name\": \"Maltese 47-key\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000043B\",\n"+
      "    \"name\": \"Norwegian with Sami\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000043F\",\n"+
      "    \"name\": \"Kazakh\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000440\",\n"+
      "    \"name\": \"Kyrgyz Cyrillic\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000444\",\n"+
      "    \"name\": \"Tatar\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000445\",\n"+
      "    \"name\": \"Bengali\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000446\",\n"+
      "    \"name\": \"Punjabi\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000447\",\n"+
      "    \"name\": \"Gujarati\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000449\",\n"+
      "    \"name\": \"Tamil\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000044A\",\n"+
      "    \"name\": \"Telugu\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000044B\",\n"+
      "    \"name\": \"Kannada\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000044C\",\n"+
      "    \"name\": \"Malayalam\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000044E\",\n"+
      "    \"name\": \"Marathi\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000450\",\n"+
      "    \"name\": \"Mongolian Cyrillic\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000452\",\n"+
      "    \"name\": \"United Kingdom Extended\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000045A\",\n"+
      "    \"name\": \"Syriac\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000461\",\n"+
      "    \"name\": \"Nepali\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000463\",\n"+
      "    \"name\": \"Pashto\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000465\",\n"+
      "    \"name\": \"Divehi Phonetic\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000046E\",\n"+
      "    \"name\": \"Luxembourgish\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000481\",\n"+
      "    \"name\": \"Maori\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000804\",\n"+
      "    \"name\": \"Chinese (Simplified) - US Keyboard\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000807\",\n"+
      "    \"name\": \"Swiss German\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000809\",\n"+
      "    \"name\": \"United Kingdom\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000080A\",\n"+
      "    \"name\": \"Latin American\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000080C\",\n"+
      "    \"name\": \"Belgian French\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000813\",\n"+
      "    \"name\": \"Belgian (Period)\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000816\",\n"+
      "    \"name\": \"Portuguese\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000081A\",\n"+
      "    \"name\": \"Serbian (Latin)\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000082C\",\n"+
      "    \"name\": \"Azeri Cyrillic\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000083B\",\n"+
      "    \"name\": \"Swedish with Sami\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000843\",\n"+
      "    \"name\": \"Uzbek Cyrillic\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000085D\",\n"+
      "    \"name\": \"Inuktitut Latin\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000C0C\",\n"+
      "    \"name\": \"Canadian French (legacy)\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000C1A\",\n"+
      "    \"name\": \"Serbian (Cyrillic)\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00000C0C\",\n"+
      "    \"name\": \"Canadian French\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00001009\",\n"+
      "    \"name\": \"Canadian English\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000100C\",\n"+
      "    \"name\": \"Swiss French\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000141A\",\n"+
      "    \"name\": \"Bosnian\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x00001809\",\n"+
      "    \"name\": \"Irish\"\n"+
      "  },\n"+
      "  {\n"+
      "    \"code\": \"0x0000201A\",\n"+
      "    \"name\": \"Bosnian Cyrillic\"\n"+
      "  }\n"+
      "]";

}
