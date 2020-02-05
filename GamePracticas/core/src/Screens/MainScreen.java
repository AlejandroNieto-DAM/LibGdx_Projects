package Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.scenes.scene2d.*;
import Actors.Koala;
import Actors.Minion;
import Actors.Planta;
import Actors.Seta;
import Actors.Bala;
import com.mygdx.game.MyGdxGame;
import java.util.ArrayList;

public class MainScreen implements Screen {
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    Koala koala;
    MyGdxGame game;
    Double tiempo;
    
    ArrayList<Bala> balls;
    ArrayList<Minion> pistol;
    ArrayList<Seta> setas;
    
    ArrayList<Integer> positionSetasX;
    ArrayList<Integer> positionSetasY;

   


    Planta a;
    
    MainScreen(MyGdxGame game){
        this.game = game;
    }

    public void show() {
        map = new TmxMapLoader().load("mapy.tmx");
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        camera = new OrthographicCamera();

        stage = new Stage();
        stage.getViewport().setCamera(camera);

        positionSetasX = new ArrayList();
        positionSetasY = new ArrayList();
        
        positionSetasX.add(24);
        positionSetasY.add(13);
        
        positionSetasX.add(57);
        positionSetasY.add(13);
        
        positionSetasX.add(109);
        positionSetasY.add(15);
        
        //positionSetasX.add(24);
        //positionSetasY.add(13);
        
        
        koala = new Koala();
        
        
        pistol = new ArrayList();
        balls = new ArrayList();
        a = new Planta();
        setas = new ArrayList();
        
        koala.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        
        
        
        koala.setPosition(221, 10);
        a.setPosition(41.2f, 6);
        
        stage.addActor(koala);
        stage.addActor(a);
        

        tiempo = 0.0;
        
        for(int i = 0; i < 10; i++){
            pistolSpawn();
        }
        
        
        for(int i = 0; i < 5; i++){
            ballSpawn();
        }
        
    }
    
    public void pistolSpawn(){
        Minion p = new Minion();
        
        p.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        double pos = Math.random()*239 + 30;
        int pos2 = (int) pos;
        
        
        p.setPosition(pos2, 40);
        stage.addActor(p);
        
        pistol.add(p);
    }
    
    public void setaSpawn(float x, float y){
        Seta p = new Seta();
        
        p.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        p.setPosition(x, y);
        stage.addActor(p);
        
        setas.add(p);
    }
    
    public void ballSpawn(){
        Bala p = new Bala();
        
        p.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        double pos = Math.random()*219 + 100;
        int pos2 = (int) pos;
        double pos3 = Math.random()*4 + 8;
        int pos24 = (int) pos3;
        
        p.setPosition(pos2, pos24);
        stage.addActor(p);
        
        balls.add(p);
    }
    
    public void checkCollisions(float delta){
        tiempo += delta;
        
        for(int i = 0;  i < pistol.size(); i++){
            
            if(pistol.get(i).dead(koala.getX(), koala.getY()) == true){

                koala.bump();
                
            }
        }
        
        for(int i = 0 ; i < positionSetasX.size(); i++){
            if(koala.getX() < positionSetasX.get(i) + 1 && koala.getX() > positionSetasX.get(i) - 1 && koala.getY() > positionSetasY.get(i) - 2 && koala.getY() < positionSetasX.get(i)){
                setaSpawn(positionSetasX.get(i), positionSetasY.get(i));
                positionSetasX.remove(i);
                positionSetasY.remove(i);                
            }
        }
        
        for(int i = 0;  i < setas.size(); i++){
            
            if(setas.get(i).dead(koala.getX(), koala.getY()) == true){
                koala.grande();  
                setas.get(i).remove();
                setas.remove(i);
            }
            
        }
        
        for(int i = 0; i < balls.size(); i++){
            
            if(koala.getX() - balls.get(i).getX() < 3 && koala.getY() - balls.get(i).getY() < 3 && balls.get(i).getX() - koala.getX() < 3 && balls.get(i).getY() - koala.getY() < 3){
               balls.get(i).positionToCome(koala.getX(), koala.getY());
            }else{
                balls.get(i).positionToCome(0, 0);
            }
        
            if(koala.getX() - balls.get(i).getX() < 1.5 && koala.getY() - balls.get(i).getY() < 1.5 && balls.get(i).getX() - koala.getX() < 1.5 && balls.get(i).getY() - koala.getY() < 1.5){
                   koala.stunt();
            }   
        }     
        
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(koala.getX() > 13 && koala.getX() < 227){
           camera.position.x = koala.getX();
           
        }
           
        if(koala.getX() < 0){
            koala.setPosition(0, koala.getY());
        }
           
        if(koala.getX() > 239){
            koala.setPosition(227, koala.getY());
        }
           
        if(koala.getY() < -20){
            game.setScreen(new LooseScreen(game));
            dispose();
        } 
        
        
        if(koala.getX() > 233){
            game.setScreen(new LooseScreen(game));
            dispose();
        }
        
        System.out.println("Koala y " + koala.getY());
        System.out.println("Koala x " + koala.getX());

        //System.out.println("Pistol y " + pistol.getY());
        //System.out.println("Pistol x " + pistol.getX());

        
        this.checkCollisions(delta);
        
        camera.update(); 
        
        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
    }

    public void hide() {
    }

    public void pause() {
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, 20 * width / height, 20);
    }

    public void resume() {
    }
}
