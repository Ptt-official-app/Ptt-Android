package tw.y_studio.ptt.Ptt;

import org.jsoup.nodes.Element;

public class WebUtils {
    public static String getElementColor(Element element) {
        if (element != null) {
            int i = 7,j=0,k=0;
            if (element.tagName().equals("span")) {
                if(element.hasClass("hl")) {
                    k=1;
                }
                if (element.className().contains("f")) {
                    i = element.className().indexOf(102);
                    i = Integer.parseInt(element.className().substring(i + 1, i + 2)) ;
                }
                if (element.className().contains("b")) {
                    int indexOf = element.className().indexOf(98);
                    j = Integer.parseInt(element.className().substring(indexOf + 1, indexOf + 2)) ;
                } else {
                    j = 0;
                }

                return (k*100+10*j+i)+"";
            }
            return "7";
        } else {
            return "";
        }

    }
}

