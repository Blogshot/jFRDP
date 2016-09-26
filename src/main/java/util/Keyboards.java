package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Keyboards extends ArrayList {

  private static ArrayList<Keyboard> keyboards;
  
  public static ArrayList<Keyboard> getKeyboards() {

    if (keyboards == null) {
      Type type = new TypeToken<ArrayList<Keyboard>>() {}.getType();

      keyboards = new Gson().fromJson(jsonString, type);
    }

    return keyboards;
  }
  
  public static String getCodeFromName(String name) {
    for (Keyboard keyboard : keyboards) {
      if (keyboard.name.equals(name)) {
        return keyboard.code;
      }
    }
    
    return "";
  }
  
  public static String getNameFromCode(String code) {
    for (Keyboard keyboard : keyboards) {
      if (keyboard.code.equals(code)) {
        return keyboard.name;
      }
    }
  
    return "NOT FOUND";
  }

  public static int indexOf(String code) {
    for (Keyboard keyboard : keyboards) {
      if (keyboard.name.equals(code) || keyboard.code.equals(code)) {
        return keyboards.indexOf(keyboard);
      }
    }

    // default to NONE
    return 0;
  }


  public static class Keyboard {
    String name;
    public String code;

    @Override
    public String toString() {
      return name;
    }
  }

  private static String jsonString = "["+
      "  {"+
      "    \"code\": \"\","+
      "    \"name\": \"NONE\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000401\","+
      "    \"name\": \"Arabic (101)\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000402\","+
      "    \"name\": \"Bulgarian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000404\","+
      "    \"name\": \"Chinese (Traditional) - US Keyboard\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000405\","+
      "    \"name\": \"Czech\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000406\","+
      "    \"name\": \"Danish\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000407\","+
      "    \"name\": \"German\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000408\","+
      "    \"name\": \"Greek\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000409\","+
      "    \"name\": \"US\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000040A\","+
      "    \"name\": \"Spanish\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000040B\","+
      "    \"name\": \"Finnish\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000040C\","+
      "    \"name\": \"French\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000040D\","+
      "    \"name\": \"Hebrew\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000040E\","+
      "    \"name\": \"Hungarian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000040F\","+
      "    \"name\": \"Icelandic\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000410\","+
      "    \"name\": \"Italian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000411\","+
      "    \"name\": \"Japanese\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000412\","+
      "    \"name\": \"Korean\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000413\","+
      "    \"name\": \"Dutch\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000414\","+
      "    \"name\": \"Norwegian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000415\","+
      "    \"name\": \"Polish (Programmers)\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000416\","+
      "    \"name\": \"Portuguese (Brazilian ABNT)\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000418\","+
      "    \"name\": \"Romanian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000419\","+
      "    \"name\": \"Russian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000041A\","+
      "    \"name\": \"Croatian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000041B\","+
      "    \"name\": \"Slovak\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000041C\","+
      "    \"name\": \"Albanian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000041D\","+
      "    \"name\": \"Swedish\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000041E\","+
      "    \"name\": \"Thai Kedmanee\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000041F\","+
      "    \"name\": \"Turkish Q\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000420\","+
      "    \"name\": \"Urdu\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000422\","+
      "    \"name\": \"Ukrainian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000423\","+
      "    \"name\": \"Belarusian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000424\","+
      "    \"name\": \"Slovenian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000425\","+
      "    \"name\": \"Estonian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000426\","+
      "    \"name\": \"Latvian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000427\","+
      "    \"name\": \"Lithuanian IBM\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000429\","+
      "    \"name\": \"Farsi\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000042A\","+
      "    \"name\": \"Vietnamese\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000042B\","+
      "    \"name\": \"Armenian Eastern\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000042C\","+
      "    \"name\": \"Azeri Latin\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000042F\","+
      "    \"name\": \"FYRO Macedonian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000437\","+
      "    \"name\": \"Georgian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000438\","+
      "    \"name\": \"Faeroese\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000439\","+
      "    \"name\": \"Devanagari - INSCRIPT\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000043A\","+
      "    \"name\": \"Maltese 47-key\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000043B\","+
      "    \"name\": \"Norwegian with Sami\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000043F\","+
      "    \"name\": \"Kazakh\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000440\","+
      "    \"name\": \"Kyrgyz Cyrillic\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000444\","+
      "    \"name\": \"Tatar\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000445\","+
      "    \"name\": \"Bengali\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000446\","+
      "    \"name\": \"Punjabi\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000447\","+
      "    \"name\": \"Gujarati\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000449\","+
      "    \"name\": \"Tamil\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000044A\","+
      "    \"name\": \"Telugu\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000044B\","+
      "    \"name\": \"Kannada\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000044C\","+
      "    \"name\": \"Malayalam\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000044E\","+
      "    \"name\": \"Marathi\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000450\","+
      "    \"name\": \"Mongolian Cyrillic\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000452\","+
      "    \"name\": \"United Kingdom Extended\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000045A\","+
      "    \"name\": \"Syriac\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000461\","+
      "    \"name\": \"Nepali\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000463\","+
      "    \"name\": \"Pashto\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000465\","+
      "    \"name\": \"Divehi Phonetic\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000046E\","+
      "    \"name\": \"Luxembourgish\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000481\","+
      "    \"name\": \"Maori\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000804\","+
      "    \"name\": \"Chinese (Simplified) - US Keyboard\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000807\","+
      "    \"name\": \"Swiss German\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000809\","+
      "    \"name\": \"United Kingdom\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000080A\","+
      "    \"name\": \"Latin American\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000080C\","+
      "    \"name\": \"Belgian French\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000813\","+
      "    \"name\": \"Belgian (Period)\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000816\","+
      "    \"name\": \"Portuguese\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000081A\","+
      "    \"name\": \"Serbian (Latin)\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000082C\","+
      "    \"name\": \"Azeri Cyrillic\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000083B\","+
      "    \"name\": \"Swedish with Sami\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000843\","+
      "    \"name\": \"Uzbek Cyrillic\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000085D\","+
      "    \"name\": \"Inuktitut Latin\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000C0C\","+
      "    \"name\": \"Canadian French (legacy)\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000C1A\","+
      "    \"name\": \"Serbian (Cyrillic)\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00000C0C\","+
      "    \"name\": \"Canadian French\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00001009\","+
      "    \"name\": \"Canadian English\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000100C\","+
      "    \"name\": \"Swiss French\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000141A\","+
      "    \"name\": \"Bosnian\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x00001809\","+
      "    \"name\": \"Irish\""+
      "  },"+
      "  {"+
      "    \"code\": \"0x0000201A\","+
      "    \"name\": \"Bosnian Cyrillic\""+
      "  }"+
      "]";

}
