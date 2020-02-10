package Map1;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.scenes.scene2d.*;
import Actors.MainActor;
import Actors.Minion;
import Actors.Planta;
import Actors.Seta;
import Actors.Bala;
import Actors.FireBall;
import Actors.FlyTortoise;
import Actors.Tortoise;
import Screens.LooseScreen;
import com.mygdx.game.MyGdxGame;
import java.util.ArrayList;
import java.util.Iterator;

public class Map1 implements Screen {
    
    public final int minionEnemys = 10;
    public final int numberFlyingTortoises = 10;
    
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    MyGdxGame game;
    Double tiempo;
    Double tiempoCheckCollisions;
    
    MainActor mainActor;
    
    ArrayList<Bala> cannonAmmo;
    ArrayList<FlyTortoise> flyingTortoises;
    
    ArrayList<Minion> minions;
    ArrayList<Seta> setas;
    ArrayList<Planta> plantas;
    ArrayList<Tortoise> tortoises;
    
    
    ArrayList<Integer> positionSetasX;
    ArrayList<Integer> positionSetasY;

    TiledMapTileLayer plants;
    
    Boolean hit;
    
    
    FireBall prueba = new FireBall();
    
    
    
    public Map1(MyGdxGame game){
        this.game = game;
    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("Map3/Pelea.tmx");
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        
        camera = new OrthographicCamera();

        stage = new Stage();
        stage.getViewport().setCamera(camera); 
  
        mainActor = new MainActor();
        mainActor.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        mainActor.setPosition(5, 5); 
        stage.addActor(mainActor);
        
        prueba.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        prueba.setPosition(10, 5); 
        stage.addActor(prueba);
        
        minions = new ArrayList();
        cannonAmmo = new ArrayList();
        plantas = new ArrayList();
        setas = new ArrayList();
        tortoises = new ArrayList();
        flyingTortoises = new ArrayList();
        
        positionSetasX = new ArrayList();
        positionSetasY = new ArrayList();
        
        tiempo = 0.0;
        tiempoCheckCollisions = 0.0;
        hit = false;
        
        
       
        //this.loadMapPlants(0, 0);
        //this.loadMapTortoises(0, 0);
        
        //this.loadMapCannonAmmo(0, 0);
        
        this.setasPositions();
        
        for(int i = 0; i < minionEnemys; i++){
            minionSpawn();
        }
        
        
        for(int i = 0; i < numberFlyingTortoises; i++){
            flyingTortoise();
        }
        
    }
    
    public void setasPositions(){
        positionSetasX.add(37); //POSICION EXACTA DE LA CAJA
        positionSetasY.add(9); //CONTANDO DESDE ABAJO
        
       // positionSetasX.add(57);
       // positionSetasY.add(13);
        
        //positionSetasX.add(109);
        //positionSetasY.add(15);
    }
    
    public void spawnTortoise(float x, float y){
        Tortoise tortoise = new Tortoise();
        
        tortoise.layer = (TiledMapTileLayer) map.getLayers().get("walls");

        tortoise.setPosition(x, y);
        stage.addActor(tortoise);
        
        tortoises.add(tortoise);

    }
    
    public void spawnPlant(float x, float y){
        Planta plant = new Planta();
        
        plant.setPosition(x + 0.5f, y);
        stage.addActor(plant);
        
        plantas.add(plant);

    }
    
    
    public void minionSpawn(){
        Minion minion = new Minion();
        
        minion.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        double pos = Math.random()*239 + 30;
        int pos2 = (int) pos;
         
        minion.setPosition(pos2, 40);
        stage.addActor(minion);
        
        minions.add(minion);
    }
    
    public void setaSpawn(float x, float y){
        Seta seta = new Seta();
        
        seta.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        
        seta.setPosition(x, y);
        stage.addActor(seta);
        
        setas.add(seta);
    }
    
    public void flyingTortoise(){
        FlyTortoise flyingTortoise = new FlyTortoise();

        
        double pos = Math.random()*219 + 100;
        int pos2 = (int) pos;
        double pos3 = Math.random()*4 + 8;
        int pos24 = (int) pos3;
        
        flyingTortoise.setPosition(pos2, pos24);
        stage.addActor(flyingTortoise);
        
        this.flyingTortoises.add(flyingTortoise);
    }
    
    public void cannonAmmoSpawn(float x, float y){
        Bala cannonAmmo = new Bala();
        
        cannonAmmo.setPosition(x - 1, y);
        stage.addActor(cannonAmmo);
        
        this.cannonAmmo.add(cannonAmmo);
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        this.mainActorLimits();
        
        tiempo += delta;
        if(tiempo > 4){
            System.out.println("ENTRAOOOO");
            //this.loadMapCannonAmmo(0, 0);
            tiempo = 0.0;
        }
        
        System.out.println("Koala y " + mainActor.getY());
        System.out.println("Koala x " + mainActor.getX());

        if(hit){
            tiempoCheckCollisions += delta;
            if(tiempoCheckCollisions > 1){
                tiempoCheckCollisions = 0.0;
                hit = false;
            }
        }
        this.checkCollisions(delta);
        
        camera.update(); 
        this.game.batch.setProjectionMatrix(camera.combined);
        
        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();
    }
    
    public void mainActorLimits(){
        if(mainActor.getX() > 13 && mainActor.getX() < 227){
           camera.position.x = mainActor.getX();
           
        }
           
        if(mainActor.getX() < 0){
            mainActor.setPosition(0, mainActor.getY());
        }
           
        if(mainActor.getX() > 239){
            mainActor.setPosition(227, mainActor.getY());
        }
           
        if(mainActor.getY() < -20){
            game.setScreen(new LooseScreen(game));
            dispose();
        } 
        
        
        if(mainActor.getX() > 233){
            game.setScreen(new LooseScreen(game));
            dispose();
        }
    }
    
    public void checkCollisions(float delta){
  
        this.setasCollisions();
        this.minionCollisions();
        this.flyingTortoisesCollisions();
        this.plantsCollisions();
        this.tortoiseCollisions();
        this.cannonAmmoCollisions();
  

    }
    
    
    public void cannonAmmoCollisions(){
        Iterator<Bala> iter = cannonAmmo.iterator();
        while(iter.hasNext()){
            Bala a = iter.next();
            
            if(a.dead(mainActor.getX(), mainActor.getY()) == true){
                mainActor.bump();
                hit = true;    
                    
                a.setY(-10f);
                iter.remove();
                cannonAmmo.remove(a);
                
            } else {
                
                if(a.getY() - 1 < mainActor.getY() && mainActor.getY() < a.getY() + 1 && a.getX() + 1f > mainActor.getX() && a.getX() - 1f < mainActor.getX() && hit == false){

                    if(mainActor.getState() == 1){
                            game.setScreen(new LooseScreen(game));
                            dispose();
                    }

                    if(mainActor.getState() == 2){
                        mainActor.chico();
                    }     
                    
                }
   
            }
        }
    }
    
    public void tortoiseCollisions(){
        Iterator<Tortoise> iter = tortoises.iterator();
        
        while(iter.hasNext()){
            Tortoise a = iter.next();
                        
            if(a.dead(mainActor.getX(), mainActor.getY()) == true){
                    mainActor.bump();
                    hit = true;      
            } else {
                
                if(a.getY() + 1f > mainActor.getY() && mainActor.getY() >= a.getY() && a.getX() + 1.2f > mainActor.getX() && a.getX() - 1.2f < mainActor.getX() && hit == false){

                    if(mainActor.getState() == 1){
                            game.setScreen(new LooseScreen(game));
                            dispose();
                    }

                    if(mainActor.getState() == 2){
                        mainActor.chico();
                    }     
                    
                }
                
                if(a.getState() == 4){
                        a.setY(-10f);
                        iter.remove();
                        tortoises.remove(a);
                }
            }   
        }
    }
    
    public void plantsCollisions(){
        Iterator<Planta> iter = plantas.iterator();

        while(iter.hasNext()){
            Planta a = iter.next();
               
            if(a.getY() + 1.5f > mainActor.getY() && a.getY() < mainActor.getY() && a.getX() + 0.8f > mainActor.getX() && a.getX() - 0.8f < mainActor.getX() && hit == false){
                
                if(mainActor.getState() == 1){
                    game.setScreen(new LooseScreen(game));
                    dispose();
                }
                
                if(mainActor.getState() == 2){
                    mainActor.chico();
                } 
                
                hit = true;
            } 
        }
    }
    
    public void minionCollisions(){
        
        Iterator<Minion> iter = minions.iterator();
        while(iter.hasNext()){
            Minion a = iter.next();
            
            if(a.dead(mainActor.getX(), mainActor.getY()) == true){
                mainActor.bump();
            }
            
            if(a.getY() < mainActor.getY() + 1.1 && a.getY() > mainActor.getY() + 0.3f && a.getX() > mainActor.getX() - 1f && a.getX() < mainActor.getX() + 1f && hit == false){
                
                if(mainActor.getState() == 2){
                    mainActor.chico();
                }
                
                if(mainActor.getState() == 1){
                    game.setScreen(new LooseScreen(game));
                    dispose();
                } 
                
                hit = true;
            } 
            
            if(a.getState() == 3){
               a.setY(-10f);
               iter.remove();
            }
            
        }
        
    }
    
    public void setasCollisions(){
        for(int i = 0 ; i < positionSetasX.size(); i++){
            if(mainActor.getX() < positionSetasX.get(i) + 0.5 && mainActor.getX() > positionSetasX.get(i) - 0.5 && mainActor.getY() > positionSetasY.get(i) - 2.5 && mainActor.getY() < positionSetasY.get(i) - 1){
                setaSpawn(positionSetasX.get(i), positionSetasY.get(i));
                positionSetasX.remove(i);
                positionSetasY.remove(i);                
            }
        }
        
        for(int i = 0;  i < setas.size(); i++){            
            if(setas.get(i).dead(mainActor.getX(), mainActor.getY()) == true){         
                if(mainActor.getState() == 1){ 
                    mainActor.grande();  
                    setas.get(i).remove();
                    setas.remove(i);
                }   
            } 
        }
    }
    
    public void flyingTortoisesCollisions(){
        for(int i = 0; i < flyingTortoises.size(); i++){
            
            if(mainActor.getX() - flyingTortoises.get(i).getX() < 3 && mainActor.getY() - flyingTortoises.get(i).getY() < 3 && flyingTortoises.get(i).getX() - mainActor.getX() < 3 && flyingTortoises.get(i).getY() - mainActor.getY() < 3){
               flyingTortoises.get(i).positionToCome(mainActor.getX(), mainActor.getY());
            }else{
                flyingTortoises.get(i).positionToCome(0, 0);
            }
        
            if(flyingTortoises.get(i).dead(mainActor.getX(), mainActor.getY()) == true){
                
                mainActor.bump();
                
            } else {
                if(flyingTortoises.get(i).getX() + 1.1f > mainActor.getX() && flyingTortoises.get(i).getX() - 1.1f < mainActor.getX() && mainActor.getY() < flyingTortoises.get(i).getY() + 1.1f && flyingTortoises.get(i).getY() - 0.6f < mainActor.getY() && hit == false){
                    
                    if(mainActor.getState() == 2){
                        mainActor.chico();
                        mainActor.stunt();
                    }
                    
                    if(mainActor.getState() == 1){
                        game.setScreen(new LooseScreen(game));
                        dispose();
                    }

 
                    hit = true;
                }
            }
            
            if(flyingTortoises.get(i).getState() == 3){
               flyingTortoises.get(i).setY(-10f);
            }
        } 
    }
    
    public void loadMapPlants(float startX, float startY) {
        
        TiledMapTileLayer plant = (TiledMapTileLayer)map.getLayers().get("plants");
        
        float endX = startX + plant.getWidth();
        float endY = startY + plant.getHeight();

        int x = (int) startX;
        while (x < endX) {
            int y = (int) startY;
            while (y < endY) {
                //System.out.println("Position x --> " + x);
                //System.out.println("Position y --> " + y);
                if (plant.getCell(x, y) != null ) {
                    if(plant.getProperties().get("plants", Boolean.class) == false){
                      plant.setCell(x, y, null);
                      spawnPlant(x, y);  
                    }      
                }
                y = y + 1;
            }
            x = x + 1;
        }
    }
    
    public void loadMapTortoises(float startX, float startY) {
        
        TiledMapTileLayer plant = (TiledMapTileLayer)map.getLayers().get("tortoises");
        
        float endX = startX + plant.getWidth();
        float endY = startY + plant.getHeight();

        int x = (int) startX;
        while (x < endX) {
            int y = (int) startY;
            while (y < endY) {
                //System.out.println("Position x --> " + x);
                //System.out.println("Position y --> " + y);
                if (plant.getCell(x, y) != null ) {
                    if(plant.getProperties().get("tortoises", Boolean.class) == false){
                      plant.setCell(x, y, null);
                      spawnTortoise(x, y);  
                    }      
                }
                y = y + 1;
            }
            x = x + 1;
        }
    }
    
    public void loadMapCannonAmmo(float startX, float startY) {
        
        TiledMapTileLayer canons = (TiledMapTileLayer)map.getLayers().get("canon");
        
        float endX = startX + canons.getWidth();
        float endY = startY + canons.getHeight();

        int x = (int) startX;
        while (x < endX) {
            int y = (int) startY;
            while (y < endY) {
                //System.out.println("Position x --> " + x);
                //System.out.println("Position y --> " + y);
                if (canons.getCell(x, y) != null ) {
                    if(canons.getProperties().get("canon", Boolean.class) == false){
                      this.cannonAmmoSpawn(x, y);  
                    }      
                }
                y = y + 1;
            }
            x = x + 1;
        }
    }
    

    @Override
    public void dispose() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 20 * width / height, 20);
    }

    @Override
    public void resume() {
    }
}
