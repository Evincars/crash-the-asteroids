package Graphics;

import java.awt.Color;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*******************************************************************************
 * Trida {@code MyColor} definuje skupinu zakladnich barev pro pouziti
 * pri kresleni tvaru pred zavedenim balicku.
 * Neni definovana jako vyctovy typ, aby si uzivatel mohl libovolne pridavat
 * vlastni barvy.
 *
 * @author Rudolf PECINOVSKY, Jan Kozusznik (preklad do anglictiny)
 * @version 3.00.001
 */
public class MyColor {
  /**
   * Pocet pojmenovanych barev se pri konstrukci nasledujicich instanci
   * nacita, a proto musi byt deklarovan pred nimi.
   */
  private static int number = 0;

//== KONSTANTNI ATRIBUTY TRIDY =================================================

  /** Mapa vsech doposud vytvorenych barev klicovana jejich nazvy. */
  private static final Map<String, MyColor> NAMES =
      new LinkedHashMap<String, MyColor>();
  /**
   * Mapa vsech doposud vytvorenych barev klicovana jejich nazvy
   * s odstranenou diakritikou.
   */
  private static final Map<String, MyColor> NAMES_BHC =
      new LinkedHashMap<String, MyColor>();
  private static final Map<Color, MyColor> COLOR =
      new LinkedHashMap<Color, MyColor>();
  private static final List<MyColor> COLORS = new ArrayList<MyColor>(32);

  /** Cerna = RGBA( 0, 0, 0, 255); */
  public static final MyColor BLACK = new MyColor(Color.BLACK, "cerna");

  /** Modra = RGBA( 0, 0, 255, 255); */
  public static final MyColor BLUE = new MyColor(Color.BLUE, "modra");

  /** Cervena = RGBA( 255, 0, 0, 255); */
  public static final MyColor RED = new MyColor(Color.RED, "cervena");

  /** Fialova = RGBA( 255, 0, 255, 255); */
  public static final MyColor MAGENTA = new MyColor(Color.MAGENTA, "fialova");

  /** Zelena = RGBA( 0, 255, 0, 255); */
  public static final MyColor GREEN = new MyColor(Color.GREEN, "green");

  /**
   * Azurova = RGBA( 0, 255, 255, 255); <br>
   * Lze pro ni pouzit i
   * textovy nazev "blankytna".
   */
  public static final MyColor AZURE = new MyColor(Color.CYAN, "azurova");

  /** Zluta = RGBA( 255, 255, 0, 255); */
  public static final MyColor YELLOW = new MyColor(Color.YELLOW, "zluta");

  /** Bila = RGBA( 255, 255, 255, 255); */
  public static final MyColor WHITE = new MyColor(Color.WHITE, "bila");

  /** Svetleseda = RGBA( 192,192,192,255 ); */
  public static final MyColor LIGHT_GRAY = new MyColor(Color.LIGHT_GRAY,
      "svetleseda"); //128 = 0x80

  /** Seda = RGBA( 128, 128, 128, 255); */
  public static final MyColor GRAY = new MyColor(Color.GRAY, "seda");

  /** Tmavoseda = RGBA( 64, 64, 64, 255); */
  public static final MyColor DARK_GRAY = new MyColor(Color.DARK_GRAY,
      "tmavoseda"); //64 = 0x40

  /** Cerna = RGBA( 255, 175, 175, 255); */
  public static final MyColor PINK = new MyColor(Color.PINK, "ruzova"); //175 = 0xAF

  /** Oranzova = RGBA( 255, 200, 0, 255); */
  public static final MyColor ORANGE = new MyColor(Color.ORANGE, "oranzova");

  //=== Barvy bez ekvivalentnich konstant in java.awt.Color

  /** Stribrna = RGBA( 216, 216, 216, 255); */
  public static final MyColor SILVER = new MyColor(0xD8, 0xD8, 0xD8, 0xFF,
      "stribrna");

  /** Zlata = RGBA( 255, 224, 0, 255); */
  public static final MyColor GOLDEN = new MyColor(0xFF, 0xE0, 0x00, 0xFF,
      "zlata");

  /** Cihlova = RGBA( 255, 102, 0, 255); */
  public static final MyColor BRICKY = new MyColor(0xFF, 0x66, 0, 0xFF,
      "cihlova");

  /** Hneda = RGBA( 153, 51, 0, 255); */
  public static final MyColor BROWN = new MyColor(0x99, 0x33, 0x00, 0xFF,
      "brown");

  /** Kremova = RGBA( 255, 255, 204, 255); */
  public static final MyColor CREAMY = new MyColor(0xFF, 0xFF, 0xCC, 0xFF,
      "kremova");

  /** Khaki = RGBA( 153, 153, 0, 255); */
  public static final MyColor KHAKI = new MyColor(0x99, 0x99, 0x00, 0xFF,
      "khaki");

  /** Ocelova = RGBA( 0, 153, 204, 255); */
  public static final MyColor STEEL = new MyColor(0x00, 0x99, 0xCC, 0xFF,
      "ocelova");

  /** Okrova = RGBA( 255, 153, 0, 255); */
  public static final MyColor OCHRE = new MyColor(0xFF, 0x99, 0x00, 0xFF,
      "okrova");

  //=== Prusvitne barvy

  /** Mlecna=RGBA( 255, 255, 255, 128 ) - polovicne prusvitna bila. */
  public static final MyColor MILCKY = new MyColor(0xFF, 0xFF, 0xFF, 0x80,
      "mlecna");

  /** Kourova = RGBA( 128, 128, 128, 128 ) - polovicne prusvitna seda. */
  public static final MyColor SMOKY = new MyColor(0x80, 0x80, 0x80, 0x80,
      "kourova");

  /** Zadna = RGBA( 0,0,0,0) - PRUHLEDNA! */
  public static final MyColor NONE = new MyColor(0, 0, 0, 0, "zadna");

  //Alternativni nazvy nekterych barev
  static {
    AZURE.addName("blankytna");
  }

//== PROMENNE ATRIBUTY TRIDY ===================================================

  /** Priznak velikosti pismen, jimiz se vypisuji nazvy barev. */
  private static boolean upperCase = false;

//== KONSTANTNI ATRIBUTY INSTANCI ==============================================

  private final String name; //Nazev barvy zadavany konstruktoru
  private final Color color; //Barva ze standardni knihovny
  private final int index = number++;

//== PROMENNE ATRIBUTY INSTANCI ================================================
//== PRISTUPOVE METODY VLASTNOSTI TRIDY ========================================
  
  /**
   * @return the index
   */
  public int getIndex() {
    return this.index;
  }
  

  /***************************************************************************
   * Vrati vektor doposud definovanych barev.
   *
   * @return Vektor doposud definovanych barev
   */
  public static MyColor[] getKnownColors() {
    return COLORS.toArray(new MyColor[COLORS.size()]);
  }

  /***************************************************************************
   * Vrati vektor retezcu s dopsud definovanymi nazvy barev.
   * Nazvu barev je vice nez definovanych barev, protoze barvy mohou mit
   * vice nazvu (a nektere jich opravdu maji nekolik).
   *
   * @return Vektor retezcu s dopsud definovanymi nazvy barev
   */
  public static String[] getKnowNames() {
    String[] nazvy = NAMES.keySet().toArray(new String[NAMES.size()]);
    Arrays.sort(nazvy, Collator.getInstance());
    if (upperCase) {
      for (int i = 0; i < nazvy.length; i++) {
        nazvy[i] = nazvy[i].toUpperCase();
      }
    }
    return nazvy;
  }

  /***************************************************************************
   * Nastavi, zda se budou nazvy barev vypisovat velkymi pismeny.
   *
   * @param upperCase
   *          {@code true} maji-li se nazvy vypisovat velkymi pismeny,
   *          jinak {@code false}
   * @return Puvdoni nastaveni
   */
  public static boolean setUpperCase(boolean upperCase) {
    boolean original = MyColor.upperCase;
    MyColor.upperCase = upperCase;
    return original;
  }

//== OSTATNI NESOUKROME METODY TRIDY ===========================================

  /***************************************************************************
   * Otevre dialogove okno, v nemz vypise vsechny doposud definovane
   * nazvy barev. Jmena jsou lexikograficky serazena.
   */
  public static void printKnownNames() {
    final int MAX_V_RADKU = 64;
    String[] nazvy = getKnowNames();
    StringBuilder sb = new StringBuilder();
    for (int i = 0, vRadku = 0; i < nazvy.length; i++) {
      String text = nazvy[i];
      int textLength = text.length();
      if ((vRadku + textLength) >= MAX_V_RADKU) {
        sb.append('\n');
        vRadku = 0;
      }
      sb.append(text);
      vRadku += textLength + 2;
      if (i < nazvy.length) {
        sb.append(", ");
      }
    }
//        System.out.println("Barvy:\n" + sb);
    IO.message(sb);
  }

  /***************************************************************************
   * Vrati kolekci doposud definovanych barev.
   *
   * @return Vektor doposud definovanych barev
   */
  public static Collection<MyColor> knownColors() {
    return Collections.unmodifiableList(COLORS);
  }

  /***************************************************************************
   * Vrati kolekci retezcu doposud definovanych nazvu barev.
   * Nazvu barev je vice nez definovanych barev, protoze barvy mohou mit
   * vice nazvu (a nektere jich opravdu maji nekolik).
   *
   * @return Vektor retezcu s dopsud definovanymi nazvy barev
   */
  public static Collection<String> knownNames() {
    return Arrays.asList(getKnowNames());
  }

//##############################################################################
//== KONSTRUKTORY A TOVARNI METODY =============================================

  /***************************************************************************
   * Vytvori instanci barvy se zadanou velikosti barevnych slozek
   * a hladinou pruhlednosti nastavovanou v kanale alfa
   * a se zadanym ceskym nazvem a nazvem bez diakritiky.
   *
   * @param red
   *          Velikost cervene slozky
   * @param green
   *          Velikost zelene slozky
   * @param blue
   *          Velikost modre slozky
   * @param alpha
   *          Hladina pruhlednosti: 0=pruhledna, 255=nepruhledna
   * @param nazev
   *          Nazev vytvorene barvy
   * @param nazevBHC
   *          Nazev bez hacku a carek
   */
  private MyColor(int red, int green, int blue, int alpha, String nazev) {
    this(new Color(red, green, blue, alpha), nazev);
  }

  /***************************************************************************
   * Vytvori barvu ekvivalentni zadane instanci tridy {@link java.awt.Color} se zadanym ceskym nazvem.
   *
   * @param c
   *          Instance tridy {@link java.awt.Color} pozadovane barvy
   * @param name
   *          Nazev vytvarene barvy; {@code null} oznaucje,
   *          ze se ma pro barvu vytvorit genericky nazev
   */
  private MyColor(Color c, String name) {
    this.color = c;
    this.name = name.toLowerCase();

    if (NAMES.containsKey(name) || COLOR.containsKey(c)) {
      throw new IllegalArgumentException("\nInterni chyba - barva " + getName()
          + " a/nebo " + getCharakteristicDec() + " jiz existuji");
    }

    NAMES.put(name, this);
    COLOR.put(this.color, this);
    COLORS.add(this);

    addNameWithoutDiacritic();
  }

//        if( (nazev == "")  ||  (nazev == null)  )
//            throw new IllegalArgumentException(
//                "Nazev barvy musi byt zadan" );
//        nazev = nazev.toLowerCase();
//        color = new Color( red, green, blue, alpha );
//        if( NAZVY.containsKey( nazev )  ||
//            COLOR.containsKey( color )  )
//        {
//            throw new IllegalArgumentException(
//                "\nBarvu nelze vytvorit - barva " + getCharakteristikaDec() +
//                " jiz existuje" );
//        }
//        BARVY.add( index, this );
//        this.nazev = nazev;
//        NAZVY.put( nazev, this );
//        COLOR.put( color, this );
//        String bhc = odhackuj( nazev );
//        if( ! nazev.equals(bhc) )
//            NAZVY.put( bhc, this );
//    }


/***************************************************************************
   * Prevede nazev barvy na prislusny objekt typu Barva.
   *
   * @param colorName
   *          Nazev pozadovane barvy -- seznam znamych nazvu
   *          je mozno ziskat zavolanim metody getZnameNazvy()
   * @return Instance tridy Barva reprezentujici zadanou barvu
   * @throws IllegalArgumentException
   *           neni-li barva (nazev) mezi znamymi
   */
  public static MyColor getColor(String colorName) {
    MyColor color = NAMES.get(colorName.toLowerCase());
    if (color != null) {
      return color;
    } else {
      throw new IllegalArgumentException("Takto pojmenovanou barvu neznam.");
    }
  }

  /***************************************************************************
   * Vytvori instanci barvy se zadanou velikosti barevnych slozeka.
   *
   * @param red
   *          Velikost cervene slozky
   * @param green
   *          Velikost zelene slozky
   * @param blue
   *          Velikost modre slozky
   * @return Barva se zadanymi velikostmi jednotlivych slozek
   */
  public static MyColor getColor(int red, int green, int blue) {
    return getColor(red, green, blue, 0xFF);
  }

  /***************************************************************************
   * Vytvori instanci nepojmenovane barvy se zadanou velikosti barevnych
   * slozeka hladinou pruhlednosti nastavovanou v kanale alfa.
   *
   * @param red
   *          Velikost cervene slozky
   * @param green
   *          Velikost zelene slozky
   * @param blue
   *          Velikost modre slozky
   * @param alpha
   *          Hladina pruhlednosti: 0=pruhledna, 255=nepruhledna
   * @return Barva se zadanymi velikostmi jednotlivych slozek
   */
  public static MyColor getColor(int red, int green, int blue, int alpha) {
    Color color = new Color(red, green, blue, alpha);
    MyColor barva = COLOR.get(color);
    if (barva != null) {
      return barva;
    }
    String nazev =
        "Barva(r=" + red + ",g=" + green + ",b=" + blue + ",a=" + alpha + ")";
    return getColor(red, green, blue, alpha, nazev);
  }

  /***************************************************************************
   * Existuje-li zadana barva mezi znamymi, vrati ji; v opacnem pripade
   * vytvori novou barvu se zadanymi parametry a vrati odkaz na ni.
   *
   * @param red
   *          Velikost cervene slozky
   * @param green
   *          Velikost zelene slozky
   * @param blue
   *          Velikost modre slozky
   * @param name
   *          Nazev vytvorene barvy
   * @return Barva se zadanym nazvem a velikostmi jednotlivych slozek
   * @throws IllegalArgumentException
   *           ma-li nektere ze znamych barev nektery
   *           ze zadanych nazvu a pritom ma jine nastaveni barevnych slozek
   *           nebo ma jiny druhy nazev.
   */
  public static MyColor getColor(int red, int green, int blue, String name) {
    return getColor(red, green, blue, 0xFF, name);
  }

  /***************************************************************************
   * Existuje-li zadana barva mezi znamymi, vrati ji; v opacnem pripade
   * vytvori novou barvu se zadanymi parametry a vrati odkaz na ni.
   *
   * @param red
   *          Velikost cervene slozky
   * @param green
   *          Velikost zelene slozky
   * @param blue
   *          Velikost modre slozky
   * @param alpha
   *          Hladina pruhlednosti: 0=pruhledna, 255=nepruhledna
   * @param name
   *          Nazev vytvorene barvy
   * @return Instance tridy Barva reprezentujici zadanou barvu.
   * @throws IllegalArgumentException
   *           ma-li nektere ze znamych barev nektery
   *           ze zadanych nazvu a pritom ma jine nastaveni barevnych slozek
   *           nebo ma jiny druhy nazev.
   */
  public static MyColor getColor(int red, int green, int blue, int alpha,
      String name) {
    name = name.trim().toLowerCase();
    if ((name == null) || name.equals("")) {
      throw new IllegalArgumentException("Barva musi mit zadan neprazdny nazev");
    }
    Color color = new Color(red, green, blue, alpha);
    MyColor barvaN = NAMES.get(name);
    MyColor barvaC = COLOR.get(color);

    if ((barvaN != null) && (barvaN == barvaC)) {
      //Je pozadovana jiz existujici barva
      return barvaN;
    }
    if ((barvaN == null) && (barvaC == null)) {
      //Je pozadovana dosud neexistujici barva
      return new MyColor(red, green, blue, alpha, name);
    }
    //Zjistime, s kterou existujici barvou pozadavky koliduji
    MyColor b = (barvaC != null) ? barvaC : barvaN;
    Color c = b.color;
    throw new IllegalArgumentException(
        "\nZadane parametry barvy koliduji s parametry existujici barvy "
            + "[existujici  zadana]:" + "\nnazev:          " + b.getName()
            + "  " + name + "\ncervena slozka: " + c.getRed() + "  " + red
            + "\nzelena  slozka: " + c.getGreen() + "  " + green
            + "\nmodra   slozka: " + c.getBlue() + "  " + blue
            + "\npruhlednost:    " + c.getAlpha() + "  " + alpha);
  }

//== NESOUKROME METODY INSTANCI ================================================

  /***************************************************************************
   * Prevede nami pouzivanou barvu na typ pouzivany kreslitkem.
   *
   * @return Instance tridy Color reprezentujici zadanou barvu
   */
  public Color getColor() {
    return this.color;
  }

  /***************************************************************************
   * Prevede nami pouzivanou barvu na typ pouzivany kreslitkem.
   *
   * @return Instance tridy Color reprezentujici zadanou barvu
   */
  public String getCharakteristicDec() {
    return String.format("%s(dec:R=%d,G=%d,B=%d,A=%d)", this.name,
        this.color.getRed(), this.color.getGreen(), this.color.getBlue(),
        this.color.getAlpha());
  }

  /***************************************************************************
   * Prevede nami pouzivanou barvu na typ pouzivany kreslitkem.
   *
   * @return Instance tridy Color reprezentujici zadanou barvu
   */
  public String getCharakteristicHex() {
    return String.format("%s(hex:R=%02X,G=%02X,B=%02X,A=%02X)", this.name,
        this.color.getRed(), this.color.getGreen(), this.color.getBlue(),
        this.color.getAlpha());
  }

  /***************************************************************************
   * Vrati nazev barvy.
   *
   * @return Retezec definujici zadanou barvu.
   */
  public String getName() {
    return (upperCase ? this.name.toUpperCase() : this.name);
  }

  /***************************************************************************
   * Vrati vektor nazvu dane barvy.
   *
   * @return Vektor navzu barvy
   */
  public String[] getNames() {
    Collection<String> nazvy = names();
    return nazvy.toArray(new String[nazvy.size()]);
  }

  /***************************************************************************
   * Vrati kolekci nazvu dane barvy.
   *
   * @return Kolekce navzu barvy
   */
  public Collection<String> names() {
    Collection<String> nazvy = new ArrayList<String>();
    for (Map.Entry<String, MyColor> entry : NAMES.entrySet()) {
      if (entry.getValue() == this) {
        nazvy.add((upperCase ? entry.getKey().toUpperCase() : entry.getKey()));
      }
    }
    return nazvy;
  }

  /***************************************************************************
   * Prida barve dalsi nazev - prezdivku.
   *
   * @param dalsiNazev
   *          Pridavany nazev, ktery se musi lisit od vsech
   *          doposud zavedenych nazvu, jejichz seznam lze ziskat
   *          zavolanim metody {@link #getKnowNames()}
   * @throws IllegalArgumentException
   *           je-li zadany nazev jiz pouzit
   */
  public void addName(String dalsiNazev) {
    dalsiNazev = dalsiNazev.toLowerCase();
    MyColor b = colorWithName(dalsiNazev);
    if (b == null) {
      NAMES.put(dalsiNazev, this);
    } else {
      throw new IllegalArgumentException("\nJmeno musi byt jedinecne. "
          + "Barva s nazvem " + dalsiNazev + " je jiz definovana.");
    }
  }

  /***************************************************************************
   * Vrati nazev barvy.
   *
   * @return Nazev barvy
   */
  @Override
  public String toString() {
    return this.name;
  }

//== SOUKROME A POMOCNE METODY TRIDY ===========================================

  /***************************************************************************
   * Vrati barvu s danym nazvem pricem je schopen ignorovat diakritiku.
   */
  private static MyColor colorWithName(String name) {
    name = name.toLowerCase();
    MyColor barva = NAMES.get(name);
    if (barva == null) {
      barva = NAMES_BHC.get(name);
    }
    return barva;
  }

  /***************************************************************************
   * Obsahuje-li nazev diakritiku, ulozi do prislusne mapy
   * jeho verzi bez diakritiky.
   */
  private void addNameWithoutDiacritic() {
    String bhc = IO.removeDiacritics(this.name);
    if (!this.name.equals(bhc)) {
      NAMES_BHC.put(bhc, this);
    }
  }

//     /***************************************************************************
//      * Vytvori ze zadanych slozek cele cislo a zabali je do typu Integer.
//      *
//      * @param s retezec urceny ke konverzi
//      * @return Integer z barevnych slozek
//      */
//     private static Integer slozky( int r, int g, int b, int a )
//     {
//         int i = ((a & 0xFF) << 24)  |
//                 ((r & 0xFF) << 16)  |
//                 ((g & 0xFF) <<  8)  |
//                 ((b & 0xFF) <<  0);
//         return Integer.valueOf( i );
//     }

//== SOUKROME A POMOCNE METODY INSTANCI ========================================
//== VNORENE A VNITRNI TRIDY ===================================================
//== TESTY A METODA MAIN =======================================================
//
//    /***************************************************************************
//     * Testovaci metoda
//     */
//    public static void test()
//    {
//        vypisZnameNazvy();
//        System.out.println( "Nazvy: " + Arrays.asList(getZnameNazvy()) );
//        System.out.println( "Barvy: " + Arrays.asList(getZnameBarvy()) );
//        for( Barva b : getZnameBarvy() )
//        {
//            System.out.println();
//            System.out.println( b.getCharakteristikaDec() );
//            System.out.println( b.getCharakteristikaHex() );
//        }
//        Barva divna = new Barva( 1, 2, 3, 255, "divna" );
//        System.out.println("\nDivna: "     + divna.getCharakteristikaDec() );
//        Barva prusvitna = getBarva( 255, 0, 0, 128, "prusvitna" );
//        System.out.println("\nPrusvitna: "+prusvitna.getCharakteristikaDec());
//        try
//        {
//            System.out.println();
//            getBarva( 1, 2, 3, 5, "divna" );
//        }catch( IllegalArgumentException iae ) {
//            System.out.println("Vyjimka: " + iae );
//            System.out.println("Vyjimka na existujici nazev vyhozena spravne");
//        }
//        try
//        {
//            System.out.println();
//            getBarva( 1, 2, 3, 255, "podivna" );
//        }catch( IllegalArgumentException iae ) {
//            System.out.println("Vyjimka: " + iae );
//            System.out.println("Vyjimka na existujici color vyhozena spravne");
//        }
//        vypisZnameNazvy();
//    }
//    /** @param ppr Paremtry prikazoveho radku - nepouzite  */
//    public static void main( String[]ppr ) {test();}  /**/
}
