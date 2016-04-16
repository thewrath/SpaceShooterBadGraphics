import java.nio.file.Paths;
import java.io.IOException;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;


public class Hero extends GameMain{
	
    private final Sprite sprite;
    private static Texture[] allTexture;
    private static Texture lifeTexture;
	private int lifes;
        
    private Sprite lifesDrawer;
        
	public Hero() throws IOException{
            this.lifes = 3;
            this.lifesDrawer = new Sprite(lifeTexture);
            this.sprite = new Sprite(allTexture[1]);
            //l'origine du sprite est en son centre 
            this.sprite.setOrigin((this.sprite.getTexture().getSize().x/2),(this.sprite.getTexture().getSize().y/2));
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
            for(int i = 0; i<this.lifes; i++){
                    lifesDrawer.setPosition(37+(i*37),26);
                    window.draw(lifesDrawer);
            }
	}
        public static void loadTexture() throws IOException{ 
            lifeTexture = new Texture();
            lifeTexture.loadFromFile(Paths.get("src/PNG/life.png"));
            
            allTexture = new Texture[2];
            for(int i=1; i < allTexture.length; i++){
                allTexture[i] = new Texture();
                allTexture[i].loadFromFile(Paths.get("src/PNG/player.png"));
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
        public int getRotation(){
            return (int)this.sprite.getRotation();
        }
        public double getAngle(){
            return Math.toRadians(this.sprite.getRotation());
        }
        public int getLifes(){
            return this.lifes;
        }
        public void removeLife(){
            this.lifes = this.lifes-1;
        }

}
