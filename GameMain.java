import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Clock;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;

public class GameMain{
	protected static RenderWindow window;
    protected static final int HEIGHT = 768;
    protected static final int WIDTH = 768;
        
    private static Clock clockEnnemie;
    private static Clock clockBalle;
    private static Clock clockGameState;
        
    private static Texture backgroundTexture;
    private static RectangleShape background;
        
    protected static Hero player;
    public static Random random = new Random();
    private static final ArrayList<Ennemie> ennemies = new ArrayList<Ennemie>();
    private static final ArrayList<Balle> balles = new ArrayList<Balle>();
        
    private static Music sound;
    private static Music laserSound;
        
    private static GameState gameState = GameState.LOGO;
        
    private static RectangleShape logo;
    private static Texture logoTexture;
    
    private static RectangleShape pause;
    private static Texture pauseTexture;
    
    private static RectangleShape gameOver;
    private static Texture gameOverTexture;
    
	public static void main(String[] args) throws IOException{
                //la fenetre 
		window = new RenderWindow();
		window.create(new VideoMode(HEIGHT,WIDTH), "SpaceShooter");
		window.setFramerateLimit(30);
                
        //le temps
        clockEnnemie = new Clock();
        clockBalle = new Clock();
        clockGameState = new Clock();
                
        //background
		backgroundTexture = new Texture();
		backgroundTexture.loadFromFile(Paths.get("src/Backgrounds/"+randInt(0,3,random)+".png"));
		background = new RectangleShape(new Vector2f(backgroundTexture.getSize().x,backgroundTexture.getSize().y));
		background.setTexture(backgroundTexture);
        //les textures 
        Hero.loadTexture();
        Ennemie.loadTexture();
        Balle.loadTexture();
        //le player 
        player = new Hero();
        player.setPosition(new Vector2f((HEIGHT/2),WIDTH-(float)(player.getSize().y)));
                
        //les sons 
        sound = new Music();
        sound.openFromFile(Paths.get("src/sound/musique.ogg"));
        sound.setVolume(20);
        sound.play();
        sound.setLoop(true);
        laserSound = new Music();
        laserSound.openFromFile(Paths.get("src/sound/laser"+randInt(1,5,random)+".ogg"));
        laserSound.setVolume(18);
        //le logo
        logoTexture = new Texture();
        logoTexture.loadFromFile(Paths.get("src/PNG/logo.png"));
        logo = new RectangleShape(new Vector2f(400,191));
        logo.setTexture(logoTexture);
        logo.setPosition(new Vector2f(WIDTH/2,HEIGHT/2));
        logo.setOrigin(new Vector2f(logoTexture.getSize().x/2,logoTexture.getSize().y/2));
           
        //la pause 
        pauseTexture = new Texture();
        pauseTexture.loadFromFile(Paths.get("src/PNG/pause.png"));
        pause = new RectangleShape(new Vector2f(HEIGHT,WIDTH));
        pause.setTexture(pauseTexture);
        pause.setPosition(new Vector2f(0,0));
        
        //le game over 
        gameOverTexture = new Texture();
        gameOverTexture.loadFromFile(Paths.get("src/PNG/gameOver.png"));
        gameOver = new RectangleShape(new Vector2f(HEIGHT,WIDTH));
        gameOver.setTexture(gameOverTexture);
        gameOver.setPosition(new Vector2f(0,0));
       
		while(window.isOpen()) {
                    //input utilisateur
		    		inputEvent();
                    //event des processus
                    processEvent();
                    if(gameState == GameState.JEU){
                        //le display des elements 
                        drawJeu();
                         
                    }
                    else if(gameState == GameState.LOGO){
                        drawLogo();
                    } 
                    else if(gameState == GameState.PAUSE){
                    	drawPause();
                    }
                    else if(gameState == GameState.GAMEOVER){
                    	drawGameOver();
                    }
		}
	}
        //input utilisateur
        private static void inputEvent() throws IOException{
            if(gameState == GameState.PAUSE || gameState == GameState.JEU){
                //si le curseur est dans l'ecran
                if(Mouse.getPosition(window).x > 0 && Mouse.getPosition(window).x < WIDTH && Mouse.getPosition(window).y > 0 && Mouse.getPosition(window).y < HEIGHT ){
                    //le joueur va vers le curseur 
                    player.move(new Vector2f(((Mouse.getPosition(window).x-player.getPosition().x)*5)/100,((Mouse.getPosition(window).y-player.getPosition().y)*5)/100));
                    player.rotate(2);
                    gameState = GameState.JEU;
                }
                else {
                    gameState = GameState.PAUSE;
                }
                //tir qaund on clique 
                if(Mouse.isButtonPressed(Mouse.Button.LEFT)){
                    //si le temps ecoule est superieur a 300 ms
                     if(clockBalle.getElapsedTime().asMilliseconds() > 300) {
                        clockBalle.restart();
                        shoot(player.getRotation());
                    }

                }
            }
        }
        //event des processus
	private static void processEvent() throws IOException{
		System.out.println(gameState);
		if(player.getLifes()<1){
			gameState = GameState.GAMEOVER;
		}
		//evenements liee a la fenetre
		for(Event event : window.pollEvents()) {
                    switch(event.type) {
                        case CLOSED:
                            window.close();
                            break;
                }
            }
            if(clockGameState.getElapsedTime().asMilliseconds() > 3000 && gameState == GameState.LOGO) {
                        gameState = GameState.JEU;   
            }   
            //si le gameState est le jeu 
            if(gameState == GameState.JEU){
                //toutes les trois secondes spawn d'un ennemie 
                if( clockEnnemie.getElapsedTime().asMilliseconds() > 500) {
                    clockEnnemie.restart();
                    spawnEnnemies();
                }
                //les ennemies suivent le joueur
                for(int i = 0; i<ennemies.size();i++){
                    ennemies.get(i).followCible();
                    ennemies.get(i).abstractRotate();
                }
                //les collisions
                //on detruis les balles qui sont plus dans la fenetre
                for(int i = 0; i<balles.size();i++){
                    balles.get(i).goToCible();
                    if(balles.get(i).getPosition().x > WIDTH || balles.get(i).getPosition().x < 0 || balles.get(i).getPosition().y > HEIGHT || balles.get(i).getPosition().y < 0){
                        balles.remove(i);
                        break;
                    }
                    //collisions balles ennemies 
                    for(int j=0;j<ennemies.size();j++){
                        if(balles.get(i).getPosition().x - balles.get(i).getSize().x/2 <= ennemies.get(j).getPosition().x + ennemies.get(j).getSize().x/2 && balles.get(i).getPosition().x + balles.get(i).getSize().x/2 >= ennemies.get(j).getPosition().x - ennemies.get(j).getSize().x/2){
                           if(balles.get(i).getPosition().y - balles.get(i).getSize().y/2 <= ennemies.get(j).getPosition().y + ennemies.get(j).getSize().y/2 && balles.get(i).getPosition().y + balles.get(i).getSize().y/2 >= ennemies.get(j).getPosition().y - ennemies.get(j).getSize().y/2){
                                ennemies.remove(j);
                                balles.remove(i);
                                break;
                            }  
                        } 
                    }
            }
            //collisions joueur ennemies 
            for(int i=0; i<ennemies.size();i++){
                if(player.getPosition().x - (player.getSize().x/2)+15 <= ennemies.get(i).getPosition().x + ennemies.get(i).getSize().x/2 && player.getPosition().x + (player.getSize().x/2)-15 >= ennemies.get(i).getPosition().x - ennemies.get(i).getSize().x/2){
                    if(player.getPosition().y - (player.getSize().y/2)+15 <= ennemies.get(i).getPosition().y + ennemies.get(i).getSize().y/2 && player.getPosition().y + (player.getSize().y/2)-15 >= ennemies.get(i).getPosition().y - ennemies.get(i).getSize().y/2){
                        ennemies.remove(i);
                        player.removeLife();
                        break;
                    }  
                } 
            }
          }        
	}
        //le display des elements
	private static void drawJeu(){
	    window.clear();
            //on dessine le fond
            drawBackground();
                //on dessine les balles 
                for(int i = 0; i<balles.size();i++){
                    balles.get(i).draw();
                }
                 //on dessine les ennemies de la liste 
                for(int i = 0; i<ennemies.size();i++){
                    ennemies.get(i).draw();
                }
                player.draw();
            //sinon on affiche l'ecran de game Over
	    window.display();

	}
        //on dessine le logo 
        private static void drawLogo(){
            window.clear(Color.RED);
            window.draw(logo);
            window.display();
        }
        //on dessine l'ecran de pause
        private static void drawPause(){
        	window.clear();
        	drawBackground();
        	window.draw(pause);
        	window.display();
        }
        //on dessine l'ecran de game over 
        private static void drawGameOver(){
        	window.clear();
        	drawBackground();
        	window.draw(gameOver);
        	window.display();
        }
        //la methode qui dessine le fond 
        private static void drawBackground(){
            for(int i = 0; i < 9; i++){
                if(i<3){
                    background.setPosition(new Vector2f(i*256,0));
                }
                else if(i<6){
                    background.setPosition(new Vector2f((i-3)*256,256));
                }
                else{
                    background.setPosition(new Vector2f((i-6)*256,512));
                }
                window.draw(background);        
            }
        }
        //la methode pour faire spawn un ennemie
        private static void spawnEnnemies() throws IOException{
            //on ne veut pas plus de 20 ennemies 
            if(ennemies.size()< randInt(90,150,random)){
                ennemies.add(new Ennemie());
            }
        }
        //la methode pour un tir
        public static void shoot(int angle) throws IOException{
            if(balles.size() < 5){
                balles.add(new Balle(angle));
                laserSound.play();
            }
        }
        //random int 
	public static int randInt(int aStart, int aEnd, Random aRandom){
            if (aStart > aEnd) {
                throw new IllegalArgumentException("Start cannot exceed End.");
            }
            long range = (long)aEnd - (long)aStart + 1;
            long fraction = (long)(range * aRandom.nextDouble());
            int randomNumber =  (int)(fraction + aStart);    
            return randomNumber;
	}	  
}
