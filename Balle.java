import java.io.IOException;
import java.nio.file.Paths;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

public class Balle extends GameMain{
    private final Sprite sprite;
    private static Texture[] allTexture;
    
    public Balle(int angle) throws IOException{
        this.sprite = new Sprite(allTexture[1]);
        //l'origine du sprite est en son centre 
        sprite.setOrigin((this.sprite.getTexture().getSize().x/2),(this.sprite.getTexture().getSize().y/2));
        this.sprite.setPosition(player.getPosition());
        this.sprite.setRotation(angle);
      
             
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
        allTexture = new Texture[2];
        for(int i=1; i < allTexture.length; i++){
            allTexture[i] = new Texture();
            allTexture[i].loadFromFile(Paths.get("src/PNG/laser.png"));
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
    public void goToCible(){
        this.sprite.move(new Vector2f((float)Math.sin(Math.toRadians(this.sprite.getRotation()))*15,(float)-Math.cos(Math.toRadians(this.sprite.getRotation()))*15));
    }
    
}