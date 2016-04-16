
import java.io.IOException;
import java.nio.file.Paths;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

public class Ennemie extends GameMain{
    private final Sprite sprite;
    private static Texture[] allTexture;
    private int angle;
    private Direction direction;
    private Vector2f distance;
    private int vitesse;
    
    public Ennemie() throws IOException{
        this.angle = randInt(-3,3,random);
        this.direction = Direction.randomDirection();
        this.vitesse = randInt(70,100,random);
        this.sprite = new Sprite(allTexture[randInt(1,allTexture.length-1,random)]);
        //l'origine du sprite est en son centre 
        sprite.setOrigin((this.sprite.getTexture().getSize().x/2),(this.sprite.getTexture().getSize().y/2));
        if(randInt(0,3,random) == 0){
          
        }
        else{
          
        }
        
        switch(randInt(0,3,random)){
            case 0:
                this.sprite.setPosition(new Vector2f(WIDTH+this.sprite.getTexture().getSize().x,randInt(0,HEIGHT,random)));
                break;
            case 1:
                this.sprite.setPosition(new Vector2f(randInt(0,WIDTH,random),HEIGHT+this.sprite.getTexture().getSize().y));
                break;
            case 2:
                this.sprite.setPosition(new Vector2f(0-this.sprite.getTexture().getSize().x,randInt(0,HEIGHT,random)));
                break;
            case 3:
                this.sprite.setPosition(new Vector2f(randInt(0,WIDTH,random),0-this.sprite.getTexture().getSize().y));
                break;
                
        }
                
    }
    public void setTexture(Texture texture){
        this.sprite.setTexture(texture);
    }
    public void setPosition(Vector2f position){
        this.sprite.setPosition(position);
    }
    public Vector2f getPosition(){
        return this.sprite.getPosition();
    }
    public void draw(){
        window.draw(sprite);
    }
    public static void loadTexture() throws IOException{ 
        allTexture = new Texture[21];
        for(int i=1; i < allTexture.length; i++){
            allTexture[i] = new Texture();
            allTexture[i].loadFromFile(Paths.get("src/PNG/ennemy ("+i+").png"));
        } 
    }
    public Vector2i getSize(){
        return sprite.getTexture().getSize();
    }
    public void move(Vector2f position){
        this.sprite.move(position);
    }
    public void rotate(int rotationAngle){
        this.sprite.rotate(rotationAngle);
    }
    public double getRotation(){
        return Math.toRadians(this.sprite.getRotation());
    }
    public void followCible(){
    	switch(this.direction){
    	case PLAYER:
    		this.distance = new Vector2f(player.getPosition().x-this.sprite.getPosition().x,player.getPosition().y-this.sprite.getPosition().y); 
            this.sprite.move(distance.x/this.vitesse,distance.y/this.vitesse);
    		break;
    	case CENTER:
    		this.distance = new Vector2f((WIDTH/2)-this.sprite.getPosition().x,(HEIGHT/2)-this.sprite.getPosition().y);
    		this.sprite.move(distance.x/this.vitesse,distance.y/this.vitesse);
    		break;
    	case RIGHTUP:
    		this.distance = new Vector2f(504-this.sprite.getPosition().x,210-this.sprite.getPosition().y);
    		this.sprite.move(distance.x/this.vitesse,distance.y/this.vitesse);
    		break;
    	case RIGHTBOTTOM:
    		this.distance = new Vector2f(504-this.sprite.getPosition().x,553-this.sprite.getPosition().y);
    		this.sprite.move(distance.x/this.vitesse,distance.y/this.vitesse);
    		break;
    	case LEFTUP:
    		this.distance = new Vector2f(231-this.sprite.getPosition().x,229-this.sprite.getPosition().y);
    		this.sprite.move(distance.x/this.vitesse,distance.y/this.vitesse);
    		break;
    	case LEFTBOTTOM:
    		this.distance = new Vector2f(238-this.sprite.getPosition().x,562-this.sprite.getPosition().y);
    		this.sprite.move(distance.x/this.vitesse,distance.y/this.vitesse);
    		break;
		default:
			break;
    	}
    }
    public void abstractRotate(){
        this.sprite.rotate(this.angle);
    }

}


