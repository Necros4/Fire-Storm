package com.mygdx.order;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;


public class GameScreen implements Screen {

	final Drop game;
	OrthographicCamera camera;
	Animation anim1,gold,fire;
	TextureRegion bucketImage,thecoin,dropImage;
	Texture heartImage;
	Texture image,stop;
	TextureAtlas atlas = PersonChange.atlas;
	TextureAtlas fireatlas = new TextureAtlas(Gdx.files.internal("atlases/ball.atlas"));
    TextureAtlas money = new TextureAtlas(Gdx.files.internal("atlases/coin.atlas"));
	TextureRegion[] frames,poi,pew;
	private State state = State.RUN;
	int why=800,hai=480,del=1;
	boolean Da=false,M=true,M2=true;
	boolean gamemode=MainMenuScreen.gamemode,mlg=MainMenuScreen.crazy;
	boolean IfFileThere = Gdx.files.local("data/bestscore.txt").exists(),file1=Gdx.files.local("data/coins.txt").exists();
	Sound dropSound;
	Music rainMusic,rains;
	Rectangle bucket;
	int musics=Options.mpack;
	int back=Options.back;
	int y=2,i,N=PersonChange.N,T1=6,T2=13;
    String Name=PersonChange.Name,N1="d",N2="s";
	int personhai=51,personwhy=24;
	Vector3 touchPos;
	FileHandle handle=Gdx.files.local("data/bestscore.txt"),handle2=Gdx.files.local("data/coins.txt");
	Array<Rectangle> raindrops;
	Array<Rectangle> heartsdrops;
	Array<Rectangle> coins;
	long lastDropTime,lastHeartTime,lastCoinTime;
	public static long dropsGatchered=10,coinsGatchered=0;
	long Time = 1000000000;
	double speed=500.0;
    public static double score=0,bestscore=0;
	float ct,pt,ft;
	public GameScreen (final Drop gam) {
		this.game = gam;
        heartImage = new Texture("heart.png");
        if(mlg){
            Name="t";N1="t";N2="t";
            N=30;T1=3;T2=24;
            del=3;
            atlas = new TextureAtlas(Gdx.files.internal("atlases/snoop.atlas"));
            money = new TextureAtlas(Gdx.files.internal("atlases/dew.atlas"));
            fireatlas = new TextureAtlas(Gdx.files.internal("atlases/illumi.atlas"));
            heartImage = new Texture("doritos.png");
        }
        MainMenuScreen.crazy=false;
        frames=new TextureRegion[N];
        for(i=0;i<N;i++)
            frames[i]=new TextureRegion(atlas.findRegion(Name+Integer.toString(i+1)));
		poi = new TextureRegion[T1];
		for(i=0;i<T1;i++)
			poi[i] = new TextureRegion(money.findRegion(N1+Integer.toString(i+1)));
		pew = new TextureRegion[T2];
		for(i=0;i<T2;i++)
			pew[i] = new TextureRegion(fireatlas.findRegion(N2+Integer.toString(i+1)));
		stop = new Texture("butt/stop.png");
		anim1 = new Animation(0.025f,frames);
		fire = new Animation(0.025f,pew);
		gold = new Animation(0.025f,poi);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, why, hai);
		touchPos = new Vector3();
		if(back==1)
			image = new Texture("backgrounds/back1.png");
		else if(back==2)
			image = new Texture("backgrounds/back2.png");
		dropSound = Gdx.audio.newSound(Gdx.files.internal("music/waterdrop.wav"));
		if (musics==1) {
			rainMusic = Gdx.audio.newMusic(Gdx.files.internal("music/undertreeinrain.mp3"));
			rains = Gdx.audio.newMusic(Gdx.files.internal("music/rains.mp3"));
		}
		else if (musics==2) {
			rainMusic = Gdx.audio.newMusic(Gdx.files.internal("music/you.mp3"));
			rains = Gdx.audio.newMusic(Gdx.files.internal("music/kasane.mp3"));
		}
		if(!IfFileThere){
		handle= Gdx.files.local("data/bestscore.txt");
			handle.writeString(Double.toString(bestscore=new BigDecimal(score).setScale(2,RoundingMode.UP).doubleValue()), false);}
		else bestscore =Double.valueOf(handle.readString());
		if(!file1){
			handle2= Gdx.files.local("data/coins.txt");
			handle2.writeString(Long.toString(coinsGatchered), false);}
		else coinsGatchered =Long.valueOf(handle2.readString());
		rainMusic.play();
		bucket = new Rectangle();
		bucket.x = why  - personwhy * 2;
		bucket.y = 0;
		bucket.width = personwhy;
		bucket.height = personhai;
		raindrops = new Array<Rectangle>();
		heartsdrops = new Array<Rectangle>();
		coins = new Array<Rectangle>();
		ft=pt=ct=0f;
	}
	public enum State
	{
		PAUSE,
		RUN,
		RESUME,
		STOPPED
	}
	private void spawnRaindrop(){
		Rectangle raindrop = new Rectangle();
		raindrop.y = MathUtils.random(0, hai-33);
		raindrop.x = 0-65;
		raindrop.width = 65;
		raindrop.height = 33;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
	private void Coin(){
		Rectangle coin = new Rectangle();
		coin.y = MathUtils.random(0, hai-24);
		coin.x = 0-28;
		coin.width = 28;
		coin.height = 24;
		coins.add(coin);
		lastCoinTime = TimeUtils.nanoTime();
	}
	private void Heart(){
		Rectangle heart = new Rectangle();
		heart.y = MathUtils.random(0, hai-42);
		heart.x = 0-45;
		heart.width = 45;
		heart.height = 42;
		heartsdrops.add(heart);
		lastHeartTime = TimeUtils.nanoTime();
	}
	@Override
	public void render (float delta) {
		switch (state) {
			case RUN: {
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					camera.update();
					ft += Gdx.graphics.getDeltaTime();
					pt += Gdx.graphics.getDeltaTime();
					ct += Gdx.graphics.getDeltaTime();
					bucketImage = anim1.getKeyFrame(pt / 2, true);
					thecoin = gold.getKeyFrame(ct / 2, true);
					dropImage = fire.getKeyFrame(ft/del, true);
					y++;
					if (y == 60000)
						y = 2;
					game.batch.setProjectionMatrix(camera.combined);
					game.batch.begin();
					game.batch.draw(image, 0, 0);
					game.font.draw(game.batch, "Hearts left: " + dropsGatchered, 0, hai);
					game.font.draw(game.batch, "Coins: " + coinsGatchered, 200, hai);
					game.font.draw(game.batch, "Score: " + score, 0, 20);
					game.font.draw(game.batch, "Best Score: " + bestscore, 200, 20);
					//game.batch.draw(stop,why-30, hai-30);
					game.batch.draw(bucketImage, bucket.x, bucket.y);
					for (Rectangle raindrop : raindrops) {
						game.batch.draw(dropImage, raindrop.x, raindrop.y);
					}
					for (Rectangle heart : heartsdrops) {
						game.batch.draw(heartImage, heart.x, heart.y);
					}
					for (Rectangle coin : coins) {
						game.batch.draw(thecoin, coin.x, coin.y);
					}
					game.batch.end();
					if (Gdx.input.isTouched()) {
						touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
						camera.unproject(touchPos);
//						if (touchPos.x <= why && touchPos.x >= why - 30 && touchPos.y <= hai && touchPos.y >= hai-30) {
//							try {
//								Thread.sleep(200);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							pause();
//						}
//						else {
							if (touchPos.x >= 0 && touchPos.x <=200) {
								if (bucket.y > 0)
									bucket.y -= 500 * Gdx.graphics.getDeltaTime();
								if (bucket.y <= 0)
									bucket.y = 0;
							}
							if (touchPos.x >= 600 && touchPos.x <=why) {
								if (bucket.y < hai-bucket.height)
									bucket.y += 500 * Gdx.graphics.getDeltaTime();
								if (bucket.y>=hai-bucket.height)
									bucket.y = hai-bucket.height;
							}
						//}
					}
					M = rainMusic.isPlaying();
					if (!M) rains.play();
					M2 = rains.isPlaying();
					if (!M && !M2) rainMusic.play();
					score += TimeUtils.millis() / 100000000000000.0;
					score = new BigDecimal(score).setScale(2, RoundingMode.UP).doubleValue();
					if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
						bucket.x -= 700 * Gdx.graphics.getDeltaTime();
					}
					if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
						bucket.x += 700 * Gdx.graphics.getDeltaTime();
					}
					if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
						bucket.y += 700 * Gdx.graphics.getDeltaTime();
					}
					if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
						bucket.y -= 700 * Gdx.graphics.getDeltaTime();
					}
					if (TimeUtils.nanoTime() - lastDropTime > Time*2) spawnRaindrop();
				if(gamemode) { if (Time < 10000) Da = true;}
				else { if (Time < 100000 / 12) Da = true;}
					if (TimeUtils.nanoTime() - lastHeartTime > Time * 10) Heart();
					if (TimeUtils.nanoTime() - lastCoinTime > Time * 6) Coin();
					Iterator<Rectangle> iter = raindrops.iterator();
					Iterator<Rectangle> iter2 = heartsdrops.iterator();
					Iterator<Rectangle> iter3 = coins.iterator();
					while (iter.hasNext()) {
						Rectangle raindrop = iter.next();
						raindrop.x += speed* Gdx.graphics.getDeltaTime();
						if(gamemode) {
							if (speed<= 0.4)
								speed+= 0.022;
							if (!Da) Time -= 15;
						}
						else {
							if (speed<= 0.12)
								speed+= 0.017;
							if (!Da) Time -= 17;
						}
						if (raindrop.x + 65 > why) iter.remove();
						if (raindrop.overlaps(bucket)) {
							dropsGatchered--;
							if (dropsGatchered == 0) {
								dropsGatchered = 3;
								game.setScreen(new LoseMenuScreen(game));
								rainMusic.pause();
								Gdx.files.local("data/coins.txt");
								handle2 = Gdx.files.local("data/coins.txt");
								handle2.writeString(Long.toString(coinsGatchered), false);
								if (score > bestscore) {
									bestscore = score;
									Gdx.files.local("data/bestscore.txt");
									handle = Gdx.files.local("data/bestscore.txt");
									handle.writeString(Double.toString(bestscore = new BigDecimal(score).setScale(2, RoundingMode.UP).doubleValue()), false);
								}
								score = 0;
							}
							iter.remove();
							dropSound.play();
						}
					}
					while (iter2.hasNext()) {
						Rectangle heart = iter2.next();
						heart.x += speed* Gdx.graphics.getDeltaTime();
						if (heart.x + 45 > why) iter2.remove();
						if (heart.overlaps(bucket)) {
							dropsGatchered++;
							iter2.remove();
						}
					}
					while (iter3.hasNext()) {
						Rectangle coin = iter3.next();
						coin.x += speed* Gdx.graphics.getDeltaTime();
						if (coin.x + 28 > why) iter3.remove();
						if (coin.overlaps(bucket)) {
							coinsGatchered++;
							iter3.remove();
						}
					}
				break;
			}
			case PAUSE: {
				this.state=State.STOPPED;
				break;
			}
			case RESUME: {
				this.state = State.RUN;
				break;
			}
			case STOPPED:{
				if (touchPos.x <= why && touchPos.x >= why - 30 && touchPos.y <= hai && touchPos.y >= hai-30) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					resume();
				}
			}
		}
	}
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
			this.state = State.PAUSE;
	}

	@Override
	public void resume() {
		this.state = State.RESUME;
	}

	@Override
	public void hide() {
	}
	@Override
	public void dispose() {
		heartImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		rains.dispose();
		image.dispose();
		game.batch.dispose();
		game.dispose();
		raindrops.clear();
		heartsdrops.clear();
		coins.clear();
	}

	@Override
	public void show() {
		rainMusic.play();
	}
}