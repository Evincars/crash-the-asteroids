package Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;



/*******************************************************************************
 * Trida {@code Platno} slouzi k jednoduchemu kresleni na virtualni platno.
 * <p>
 * Trida neposkytuje verejny konstruktor, protoze chce, aby jeji instance
 * byla jedinacek, tj. aby se vsechno kreslilo na jedno a to same platno.
 * Jedinym zpusobem, jak ziskat odkaz na instanci tridy Platno,
 * je volani staticke metody {@link getInstance()}.</p>.
 * <p>
 * Aby bylo mozno na platno obycejne kreslit a nebylo nutno kreslene objekty
 * prihlasovat, odmazane casti obrazcu se automaticky neobnovuji.
 * Je-li proto pri smazani nektereho obrazce odmazana cast jineho obrazce,
 * je treba prislusny obrazec explicitne prekreslit.</p>
 *
 * @author   Rudolf PECINOVSKY
 * @version  3.00.002
 */
public final class Canvas
{

    /** Titulek v zahlavi okna platna. */
    private static final String TITLE  = "Crash the Asteroids";
    
    /** Pocatecni sirka platna v bodech. */
    private static final int WIDTH_0 = 500;

    /** Pocatecni vyska platna v bodech. */
    private static final int HEIGHT_0 = 550;

    /** Pocatecni barva pozadi platna. */
    private static final MyColor BACKGROUND_0 = MyColor.BLACK;

    /** Jedina instance tridy Platno. */
    private static Canvas singleton = new Canvas();  

    /** Aplikacni okno animacniho platna. */
    private JFrame windows;

    /** Instance lokalni tridy, ktera je zrizena proto, aby odstinila
     *  metody sveho rodice JPanel. */
    private JPanel ownCanvas;

    /** Vse se kresli na obraz - ten se snadneji prekresli. */
    private Image imageOfCanvas;

    /** Kreslitko ziskane od obrazu platna, na nejz se vlastne kresli. */
    private Graphics2D graphics;

    private MyColor colorOfBackground;
    private int width;
    private int height;

    /** Pozice platna na obrazovace - pri pouzivani vice obrazovek
     *  je obcas treba ji po zviditelneni obnovit. */
    Point position = new Point(0, 0);
    
    int explosionRows = 5, explosionCols = 5;
    int explosionWidth = 320, explosionHeight = 320;
    
    private int menuPointerPostion = 3;
    private String[] urls = {
    		"assets/logo.png", 
    		"assets/new_game.png", 
    		"assets/controls.png",
    		"assets/end_game.png",
    		"assets/controls_menu.png",
    		"assets/spaceship.png",
    		"assets/bg.jpg",
    		"assets/asteroid_1.png",
    		"assets/asteroid_2.png",
    		"assets/asteroid_3.png",
    		"assets/asteroid_4.png",
    		"assets/explosion.png"
    		};
    private BufferedImage[] imgs = new BufferedImage[ urls.length ];
    BufferedImage[] explosion = new BufferedImage[explosionCols * explosionRows];
    private int[] urlsMenuIndexes = {0,1,2,3,4};
    private int urlsPointerIndex = 5;
    private bgXY bg = new bgXY();
    private Font HUDFont = new Font("Arial", Font.BOLD, 22);
    private Font MenuFont = new Font("Arial", Font.BOLD, 32);
    private Font MenuSmaller = new Font("Arial", Font.PLAIN, 28);
    private Font ScoreFont = new Font("Arial", Font.PLAIN, 20);
    private Font AsteroidFont = new Font("Arial", Font.BOLD, 20);


    /***************************************************************************
     * Smaze platno, presneji smaze vsechny obrazce na platne.
     * Tato metoda by mela byr definovana jako metodoa instance,
     * protoze je instance jedinacek,
     * byla metoda pro snazsi dostupnost definovana jako metoda tridy.
     * Jinak by totiz bylo potreba vytvorit pred smazanim platna jeho instanci.
     */
    public static void clearCanvas()
    {
        singleton.erase();
    }

    /***************************************************************************
     * Jedina metoda umoznujici ziskat odkaz na instanci platna.
     * Vraci vsak pokazde odkaz na stejnou instanci, protoze tato instance
     * je jedinacek. Pokud instance pri volani metody jeste neexistuje,
     * metoda instanci vytvori.
     *
     * @return Odkaz na instanci tridy Platno.
     */
    public static Canvas getInstance()
    {
        singleton.setVisible(true);
        return singleton;
    }


    /***************************************************************************
     * Implicitni (a jediny) konstruktor - je volan pouze jednou.
     */
    @SuppressWarnings("serial")
	private Canvas()
    {
        initialization();
        windows  = new JFrame();     
        windows.setLocation( position );
        windows.setTitle(TITLE);
        
        // Change favicon
        java.net.URL url = ClassLoader.getSystemResource("assets/spaceship.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        windows.setIconImage(img);
        
        // JFrame to center of screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        windows.setLocation((dim.width/2-windows.getSize().width/2) - WIDTH_0 / 2, (dim.height/2-windows.getSize().height/2) - HEIGHT_0 / 2);
     
        // Enable close the window
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        windows.setResizable( false );   
        ownCanvas = new JPanel()
        {   // Povinne prekryvana abstraktni metoda tridy JPanel.
        	@Override
            public void paint(Graphics g) {
                paintComponent(g);
            }
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(imageOfCanvas, 0, 0, null);
                //repaint();
            }
        };
        ownCanvas.setIgnoreRepaint(true);
        windows.setContentPane(ownCanvas);
        colorOfBackground = BACKGROUND_0;
        setSize(WIDTH_0, HEIGHT_0);        //Pripravi a vykresli prazdne platno
        IO.windowsOn(position.x, position.y + windows.getSize().height);
        
        this.loadTextures();
        
        this.drawMenu();
        this.drawPointer("down");
        
    }
    
    private void loadTextures(){
    	for (int i = 0; i < urls.length; i++){
    		try {
    		    imgs[i] = ImageIO.read( ClassLoader.getSystemResource(urls[i]) );
    		} catch (IOException e) {
    		    System.out.println("Texture: " + urls[i] + " cannot be loaded");
    		}
    	}
    	//this.loadExplosion();
    }
    
    private void loadExplosion(){
    	for (int i = 0; i < explosionRows; i++)
    	{
    	    for (int j = 0; j < explosionCols; j++)
    	    {
    	        this.explosion[(i * explosionCols) + j] = imgs[11].getSubimage(
    	            j * explosionWidth,
    	            i * explosionHeight,
    	            explosionWidth,
    	            explosionHeight
    	        );
    	    }
    	}
    }
    
    public void drawMenu(){
    	int x[] = {45, 137, 137, 137, 137};
    	int y[] = {70, 275, 325, 375, 425};
    	String text[] = {"New game", "Score table", "Controls", "End game"};
    	graphics.drawImage(imgs[0], x[0], y[0], null);
    	for (int i = 1; i <= text.length; i++){
    		//graphics.drawImage(imgs[urlsMenuIndexes[i]], x[urlsMenuIndexes[i]], y[urlsMenuIndexes[i]], null);
    		setColorOfForeground(MyColor.WHITE);
            graphics.setFont(this.MenuFont);
    		graphics.drawString(text[i-1], x[i], y[i]);
    	}
    }
    
    public void drawShoots(List<Bullet> b){
    	for (int i = 0; i < b.size(); i++){
    		if (b.get(i).type == "special"){
    			setColorOfForeground(MyColor.GOLDEN);
    			graphics.drawRect(b.get(i).x, b.get(i).y, 2, 30);
    		} else if (b.get(i).type == "normal"){
    			setColorOfForeground(MyColor.GREEN);
    			graphics.drawRect(b.get(i).x, b.get(i).y, 2, 30);
    		}
    	}
    }
    
    public void drawBonuses(List<Bonus> b){
    	for (int i = 0; i < b.size(); i++){
    		if (b.get(i).type == "lives"){
    			this.drawString("❤", b.get(i).x, b.get(i).y, MyColor.RED);
			} else if (b.get(i).type == "basicBullet"){
				this.drawString("⦾ ", b.get(i).x, b.get(i).y, MyColor.WHITE);
			} else if (b.get(i).type == "specialBullet"){
				this.drawString("⦿", b.get(i).x, b.get(i).y, MyColor.GOLDEN);
			}
    	}
    }
    
    public void drawAsteroids(List<Asteroid> a){
    	for (int i = 0; i < a.size(); i++){
    		int witchTexture = 6 + Integer.parseInt(a.get(i).type);
    		graphics.drawImage(imgs[witchTexture], a.get(i).x, a.get(i).y, a.get(i).width, a.get(i).height, null);
    		setColorOfForeground(MyColor.YELLOW);
            graphics.setFont(this.MenuSmaller);
            graphics.drawString(""+a.get(i).lives, (int)(a.get(i).x + (a.get(i).width / 2) - 10), (int)(a.get(i).y + (a.get(i).height / 2) + 7));
    	}
    }
    
    public boolean drawControlsMenu(){
    	if (this.menuPointerPostion == 2){
	    	//int last = urlsMenuIndexes.length;
	    	//graphics.drawImage(imgs[0], 45, 70, null);
	    	//graphics.drawImage(imgs[4], 80, 240, null);
	    	
    		graphics.drawImage(imgs[0], 45, 70, null);
    		int yBasic = 275;
	    	setColorOfForeground(MyColor.WHITE);
            graphics.setFont(this.MenuSmaller);
    		graphics.drawString("Move ...... Arrows/WSAD", 80, yBasic);
    		graphics.drawString("Shoot ..... Space", 80, yBasic + 30);
    		graphics.drawString("Confirm ... Enter", 80, yBasic + 60);
    		graphics.drawString("End Game .. ESC", 80, yBasic + 90);
    		setColorOfForeground(MyColor.GRAY);
    		graphics.drawString("Back .... Up/Down arrow", 80, yBasic + 140);
	    	return true;
    	} else return false;
    }
    
    public boolean drawScoreMenu(){
    	if (this.menuPointerPostion == 1){
    		graphics.drawImage(imgs[0], 45, 70, null);
    		List<ScoreTable> score = new ArrayList<ScoreTable>();
    		BufferedReader br = null;
    		String[] parts;
    		try {
    			String sCurrentLine;
    			br = new BufferedReader(new FileReader("score.csv"));
    			while ((sCurrentLine = br.readLine()) != null) {
    				parts = sCurrentLine.split(";");
    				score.add(new ScoreTable(parts[0], Integer.parseInt(parts[1])));
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		} finally {
    			try {
    				if (br != null)br.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		}
    		
    		for (int i = 0; i < score.size() - 1; i++) {
                for (int j = 0; j < score.size() - i - 1; j++) {
                    if(score.get(j).score < score.get(j + 1).score){
                        ScoreTable tmp = score.get(j);
                        score.set(j, score.get(j+1));
                        score.set(j + 1, tmp);
                    }
                }
            }
    		
    		int yBasic = 250, yCounter = 40;
	    	setColorOfForeground(MyColor.WHITE);
            graphics.setFont(this.ScoreFont);
    		graphics.drawString("Top 10 score", 80, yBasic);
    		graphics.drawString("_____________________________", 80, yBasic + 7);
    		for (int i = 0; i < score.size(); i++){
    			if (i < 10){
    				if (score.get(i).date.length() == 15){
    					graphics.drawString(score.get(i).date+"      "+score.get(i).score, 80, yBasic + yCounter);
    				} else 
    					graphics.drawString(score.get(i).date+"    "+score.get(i).score, 80, yBasic + yCounter);
    				yCounter += 20;
    			}
    		}
    		setColorOfForeground(MyColor.GRAY);
    		graphics.drawString("Back .... Up/Down arrow", 80, yBasic + 265);
	    	return true;
    	} else return false;
    }
    
    private class ScoreTable {
    	public ScoreTable(String date_, int score_){
    		date = date_;
    		score = score_;
    	}
    	String date;
    	int score;
    }
    
    public void drawEndGame(int score){
    	setColorOfForeground(MyColor.RED);
    	graphics.setFont(this.MenuSmaller);
		graphics.drawString("GAME OVER", 157, this.height / 2 - 40);
		setColorOfForeground(MyColor.WHITE);
		graphics.drawString("Score: " + score, 117, this.height / 2);
		setColorOfForeground(MyColor.GRAY);
		graphics.drawString("Enter to continue...", 117, this.height / 2 + 40);
    }
    
    public void drawPointer(String choose){
    	int y = 0;
    	int[] yPos = {247, 297, 347, 397};
    	if (choose == "up") {
    		if (this.menuPointerPostion == 0){
    			this.menuPointerPostion = 3;
    			y = yPos[this.menuPointerPostion];
    		} else {
    			this.menuPointerPostion--;
    			y = yPos[this.menuPointerPostion];
    		}
    	} else if (choose == "down"){
    		if (this.menuPointerPostion == 3){
    			this.menuPointerPostion = 0;
    			y = yPos[this.menuPointerPostion];
    		} else {
    			this.menuPointerPostion++;
    			y = yPos[this.menuPointerPostion];
    		}
    	} else if (choose == null){
    		y = yPos[this.menuPointerPostion];
    	}
    	graphics.drawImage(imgs[urlsPointerIndex], 90, y, 30, 30, null);
    }
    
    
    public boolean endGame(){
    	if (this.menuPointerPostion == 3){
    		windows.dispose();
    		return true;
    	}
    	return false;
    }
    
    public void CanvasRepaint(){
    	ownCanvas.repaint();
    }
    
    public int getCursorPosition(){
    	return this.menuPointerPostion;
    }
    
    
    public void drawBackground(){
    	double speed = 0.5;
    	this.bg.y1 += speed;
    	this.bg.y2 += speed;
    	this.bg.y3 += speed;
    	
    	graphics.drawImage(imgs[6], 0, (int)this.bg.y1, null);
    	graphics.drawImage(imgs[6], 0, (int)this.bg.y2, null);
    	graphics.drawImage(imgs[6], 0, (int)this.bg.y3, null);
    	
    	if (this.bg.y3 == 0){
    		this.bg.y1 = 0 - this.bg.getBgHeight();
    		this.bg.y2 = this.bg.y1 - this.bg.getBgHeight();
    	}
    	if (this.bg.y3 >= HEIGHT_0){
    		this.bg.y3 = this.bg.y2 - this.bg.getBgHeight();
    	}
    }
    
    public void addKeyListener(KeyListener listener) {
    	windows.addKeyListener(listener);
    }
    
    public void drawSpaceship(double x, double y, double w, double h){
    	graphics.drawImage(imgs[5], (int)x, (int)y, (int)w, (int)h, null);
    }

    
    /***************************************************************************
     * Poskytuje informaci o aktualni viditelnosti okna.
     *
     * @return Je-li okno viditelne, vraci <b>true</b>, jinak vraci <b>false</b>
     */
    public boolean isVisible()
    {
        return windows.isVisible();
    }


    /***************************************************************************
     * Nastavi viditelnost platna.
     *
     * @param visible {@code true} ma-li byt platno viditelne, 
     *                  {@code false} ma-li naopak prestat byt viditelne
     */
    public synchronized void setVisible(boolean visible)
    {
        boolean zmena = (isVisible() != visible);
        if( zmena ) {
            position = windows.getLocation();
            windows.setVisible(visible);
            if( visible )
            {
                //Pri vice obrazovkach po zviaditelneni blbne =>
                windows.setLocation(position);   //je treba znovu nastavit pozici
                windows.setAlwaysOnTop(true);
                windows.toFront();
                windows.setAlwaysOnTop(false);
            }
        }
    }


    /***************************************************************************
     * Vrati aktualni barvu pozadi.
     *
     * @return   Nastavena barva pozadi
     */
    public MyColor getColorOfBackground()
    {
        return colorOfBackground;
    }


    /***************************************************************************
     * Nastavi pro platno barvu pozadi.
     *
     * @param color  Nastavovana barva pozadi
     */
    public void setColorOfBackground(MyColor color)
    {
        colorOfBackground = color;
        graphics.setBackground( colorOfBackground.getColor() );
        erase();
    }


    /***************************************************************************
     * Nastavi pro platno barvu popredi.
     *
     * @param  color  Nastavovana barva popredi
     */
    public void setColorOfForeground(MyColor color)
    {
        graphics.setColor( color.getColor() );
    }


    /***************************************************************************
     * Vrati sirku platna.
     *
     * @return  Aktualni sirka platna v bodech
     */
    public int getWidth()
    {
        return width;
    }


    /***************************************************************************
     * Vrati vysku platna.
     *
     * @return  Aktualni vyska platna v bodech
     */
    public int getHeight()
    {
        return height;
    }


    /***************************************************************************
     * Nastavi novy rozmer platna zadanim jeho vysky a sirky.
     *
     * @param  height  Nova sirka platna v bodech
     * @param  width  Nova vyska platna v bodech
     */
    public void setSize(int height, int width)
    {
        boolean upravit;
        do
        {
            this.width = height;
            this.height = width;
            windows.setResizable(true);
            ownCanvas.setPreferredSize( new  java.awt.Dimension(height, width) );
            windows.setMaximizedBounds( new java.awt.Rectangle (height, width) );
            windows.pack();
            java.awt.Dimension dim = windows.getSize( null );
            java.awt.Insets    ins = windows.getInsets();
//            IO.zprava(
//                   "Nastavuju: sirka=" + sirka + ", vyska=" + vyska +
//                 "\nMam: width=" + dim.width + ", height=" + dim.height +
//                 "\nleft=" + ins.left + ", right=" + ins.right +
//                 "\n top=" + ins.top + ", bottom=" + ins.bottom );
            upravit = false;
            if( height < (dim.width - ins.left - ins.right) )
            {
                height = dim.width - ins.left - ins.right + 2;
                upravit = true;
            }
            if( width < (dim.height - ins.top - ins.bottom) )
            {
                width = dim.height - ins.top - ins.bottom;
                upravit = true;
            }
        }while( upravit );
        imageOfCanvas = ownCanvas.createImage(height+2, width+2);
        graphics = (Graphics2D)imageOfCanvas.getGraphics();
        graphics.setBackground( colorOfBackground.getColor() );
        windows.setResizable(false);
        setVisible(true);
        preparePicture();
        erase();
    }



//== OSTATNI NESOUKROME METODY INSTANCI ========================================

    /***************************************************************************
     * Prevede instanci na retezec. Pouziva se predevsim pri ladeni.
     *
     * @return Retezcova reprezentace dane instance.
     */
    @Override
    public String toString()
    {
        return this.getClass().getName() +
            "(" + width + "" + height +
            " bodu, barvaPozadi=" + colorOfBackground + ")";
    }


    /***************************************************************************
     * Nakresli zadany obrazec a vybarvi jej barvou popredi platna.
     *
     * @param  shape  Kresleny obrazec
     */
    public void fill(Shape shape)
    {
        graphics.fill(shape);
        //ownCanvas.repaint();
    }


    /***************************************************************************
     * Smaze platno, presneji smaze vsechny obrazce na platne.
     */
    public void erase()
    {
        erase( new Rectangle2D.Double(0, 0, width, height) );
    }


    /***************************************************************************
     * Smaze zadany obrazec na platne; obrazec vsak stale existuje,
     * jenom neni videt. Smaze se totiz tak, ze se nakresli barvou pozadi.
     *
     * @param  shape   Obrazec, ktery ma byt smazan
     */
    public void erase(Shape shape)
    {
        Color original = graphics.getColor();
        graphics.setColor(colorOfBackground.getColor());
        graphics.fill(shape);       //Smaze jej vyplnenim barvou pozadi
        graphics.setColor(original);
    }


    /***************************************************************************
     * Vypise na platno text aktualnim pismem a aktualni barvou popredi.
     *
     * @param text   Zobrazovany text
     * @param x      x-ova souradnice textu
     * @param y      y-ova souradnice textu
     * @param color  Barva, kterou se zadany text vypise
     */
    public void drawString(String text, int x, int y, MyColor color)
    {
        setColorOfForeground(color);
        graphics.setFont(this.HUDFont);
        graphics.drawString(text, x, y);
        
    }


    /***************************************************************************
     * Nakresli na platno usecku se zadanymi krajnimi body.
     * Usedku vykresli aktualni barvou popredi.
     *
     * @param  x1    x-ova souradnice pocatku
     * @param  y1    y-ova souradnice pocatku
     * @param  x2    x-ova souradnice konce
     * @param  y2    x-ova souradnice konce
     * @param  color Barva usecky
     */
    public void drawLine(int x1, int y1, int x2, int y2, MyColor color)
    {
        setColorOfForeground(color);
        graphics.drawLine(x1, y1, x2, y2);
        ownCanvas.repaint();
    }



//== SOUKROME A POMOCNE METODY TRIDY ===========================================
//== SOUKROME A POMOCNE METODY INSTANCI ========================================

    /***************************************************************************
     * Inicializuje nektere parametry z konfiguracniho souboru.
     * Tento soubor je umisten v domovskem adresari uzivatele
     * ve slozce {@code .rup} v souboru {@code bluej.properties}.
     */
    private void initialization() {
        Properties sysProp = System.getProperties();
        String     userDir = sysProp.getProperty("user.home");
        File       rupFile = new File( userDir, ".rup/bluej.properties");
        Properties rupProp = new Properties();
        try {
            Reader reader = new FileReader(rupFile);
            rupProp.load(reader);
            reader.close();
            String sx = rupProp.getProperty("canvas.x");
            String sy = rupProp.getProperty("canvas.y");
            int x = Integer.parseInt(rupProp.getProperty("canvas.x"));
            int y = Integer.parseInt(rupProp.getProperty("canvas.y"));
            position = new Point( x, y );
        }catch( Exception e )  {
            position = new Point( 0, 0 );
        }
    }


    /***************************************************************************
     * Pripravi obrazek, do nejz se budou vsechny tvary kreslit.
     */
    private void preparePicture()
    {
        imageOfCanvas = ownCanvas.createImage(width, height);
        graphics = (Graphics2D)imageOfCanvas.getGraphics();
        graphics.setColor(colorOfBackground.getColor());
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.black);
    }
    
    private class bgXY {
    	private double bgHeight = 1080;
    	public double getBgHeight(){ return this.bgHeight; }
    	public double y1 = HEIGHT_0 - this.bgHeight;
    	public double y2 = HEIGHT_0 - this.bgHeight * 2;
    	public double y3 = HEIGHT_0 - this.bgHeight * 3;
    }
    
}

