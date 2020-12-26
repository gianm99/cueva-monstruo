package com.cueva.monstruo.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cueva.monstruo.CuevaDelMonstruo;
import com.cueva.monstruo.entitys.Cuadro;
import com.cueva.monstruo.entitys.Cueva;
import com.cueva.monstruo.entitys.Posicion;
import org.jetbrains.annotations.NotNull;

public class MainScreen implements Screen {
    final CuevaDelMonstruo game;
    private Cueva cueva;
    private Texture agente;
    private Texture monstruo;
    private Texture precipicio;
    private Texture suelo;
    private Texture tesoroCerrado;
    private Texture tesoroAbierto;
    private OrthographicCamera camara;
    InputMultiplexer multiplexer;
    private Stage stage;
    private Skin skin;
    private SelectBox<Integer> selectDimension;
    private ImageButton botonTesoro;
    private ImageButton botonMonstruo;
    private ImageButton botonPrecipicio;
    private ImageButton botonNext;
    private ImageButton botonAuto;
    private ImageButton botonFastForward;
    private Estado estado;
    private ModoConfig modoConfig;
    private boolean autoPlay;
    private boolean jugable;
    private long lastMoveTime;
    private static final int OFFSET_LEFT = 50;
    private static final int GAME_WIDTH = 480;
    private static final int GAME_HEIGHT = 480;

    public MainScreen(CuevaDelMonstruo game) {
        this.game = game;
        this.cueva = game.cueva;
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        //inicializar varios componentes
        init();
        //cargar las texturas
        agente = escalarTextura(new Pixmap(Gdx.files.internal("agent.png")));
        monstruo = escalarTextura(new Pixmap(Gdx.files.internal("monster.png")));
        precipicio = escalarTextura(new Pixmap(Gdx.files.internal("trap.png")));
        suelo = escalarTextura(new Pixmap(Gdx.files.internal("floor.png")));
        tesoroCerrado = escalarTextura(new Pixmap(Gdx.files.internal("chest_closed.png")));
        tesoroAbierto = escalarTextura(new Pixmap(Gdx.files.internal("chest_open.png")));
        //menú izquierdo
        //tamaño de la cueva
        selectDimension = new SelectBox<>(skin);
        selectDimension.setItems(3, 4, 5, 6, 8, 10, 12, 15, 16, 20); //tamaños seguros
        selectDimension.setSize(30, 15);
        selectDimension.setSelected(game.getPreferences().getGameSize());
        selectDimension.setPosition(10, 376);
        //botón tesoro
        Texture tesoro32 = new Texture(Gdx.files.internal("chest_closed.png"));
        Drawable drawableTesoro = new TextureRegionDrawable(new TextureRegion(tesoro32));
        botonTesoro = new ImageButton(drawableTesoro);
        botonTesoro.setPosition(9, 276);
        //botón monstruo
        Texture monstruo32 = new Texture(Gdx.files.internal("monster.png"));
        Drawable drawableMonstruo = new TextureRegionDrawable(new TextureRegion(monstruo32));
        botonMonstruo = new ImageButton(drawableMonstruo);
        botonMonstruo.setPosition(9, 176);
        //botón precipicio
        Texture precipicio32 = new Texture(Gdx.files.internal("trap.png"));
        Drawable drawablePrecipicio = new TextureRegionDrawable(new TextureRegion(precipicio32));
        botonPrecipicio = new ImageButton(drawablePrecipicio);
        botonPrecipicio.setDisabled(true);
        botonPrecipicio.setPosition(9, 76);
        //menú derecho
        //botón next
        Texture next36 = new Texture(Gdx.files.internal("botones/next.png"));
        Drawable drawableNext = new TextureRegionDrawable(new TextureRegion(next36));
        botonNext = new ImageButton(drawableNext);
        botonNext.setDisabled(true);
        botonNext.setPosition(GAME_WIDTH + 100 - 36 - 7, 369);
        //botón play
        Texture auto36 = new Texture(Gdx.files.internal("botones/auto.png"));
        Drawable drawableAuto = new TextureRegionDrawable(new TextureRegion(auto36));
        botonAuto = new ImageButton(drawableAuto);
        botonAuto.setDisabled(true);
        botonAuto.setPosition(GAME_WIDTH + 100 - 36 - 7, 222);
        //botón fast forward
        Texture fast36 = new Texture(Gdx.files.internal("botones/auto-fast.png"));
        Drawable drawableFast = new TextureRegionDrawable(new TextureRegion(fast36));
        botonFastForward = new ImageButton(drawableFast);
        botonFastForward.setDisabled(true);
        botonFastForward.setPosition(GAME_WIDTH + 100 - 36 - 7, 75);
        //añadir los botones al stage
//        stage.addActor(labelDimension);
        stage.addActor(selectDimension);
        stage.addActor(botonTesoro);
        stage.addActor(botonMonstruo);
        stage.addActor(botonPrecipicio);
        stage.addActor(botonNext);
        stage.addActor(botonAuto);
        stage.addActor(botonFastForward);
        //input del usuario
        selectDimension.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getPreferences().setGameSize(selectDimension.getSelected());
                game.cueva = new Cueva(selectDimension.getSelected());
                cueva = game.cueva;
                cambiarEstado(Estado.CONFIG1);
            }
        });
        botonTesoro.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (modoConfig != ModoConfig.TESORO) {
                    modoConfig = ModoConfig.TESORO;
                } else {
                    modoConfig = ModoConfig.NADA;
                }
            }
        });
        botonMonstruo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (modoConfig != ModoConfig.MONSTRUO) {
                    modoConfig = ModoConfig.MONSTRUO;
                } else {
                    modoConfig = ModoConfig.NADA;
                }
            }
        });
        botonPrecipicio.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (modoConfig != ModoConfig.PRECIPICIO) {
                    modoConfig = ModoConfig.PRECIPICIO;
                } else {
                    modoConfig = ModoConfig.NADA;
                }
            }
        });
        cambiarEstado(Estado.CONFIG1);
    }

    /**
     * Inicializa la información del juego cuando se cambia a esta pantalla
     */
    private void init() {
        jugable = false;
        modoConfig = ModoConfig.NADA;
        stage.clear();
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                Vector3 vector3 = new Vector3(x, y, 0);
                camara.unproject(vector3);
                x = (int) vector3.x;
                y = (int) vector3.y;
                if (x < OFFSET_LEFT || x > GAME_WIDTH + OFFSET_LEFT) {
                    return false;
                }
                Posicion posicion = cueva.calcularPosicion(x - OFFSET_LEFT, y);
                if (!cueva.posicionCorrecta(posicion)) {
                    return false;
                }
                int fila = posicion.getFila();
                int columna = posicion.getColumna();
                switch (modoConfig) {
                    case TESORO:
                        if (cueva.agregarTesoro(fila, columna) && !jugable) {
                            jugable = true;
                            cambiarEstado(Estado.CONFIG2);
                        }
                        break;
                    case MONSTRUO:
                        cueva.agregarMonstruo(fila, columna);
                        break;
                    case PRECIPICIO:
                        cueva.agregarPrecipicio(fila, columna);
                        break;
                    case NADA:
                        return false;
                }
                return true;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
        skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        autoPlay = false;
        //crear la cámara
        camara = new OrthographicCamera();
        camara.setToOrtho(false, 580, 480);
    }

    /**
     * Cambia el estado del programa
     *
     * @param nuevo el estado al que se quiere pasar
     */
    private void cambiarEstado(@NotNull Estado nuevo) {
        switch (nuevo) {
            case CONFIG1:
                autoPlay = false;
                jugable = false;
                modoConfig = ModoConfig.NADA;
                botonTesoro.setDisabled(false);
                botonMonstruo.setDisabled(true);
                botonPrecipicio.setDisabled(true);
                //escalar las texturas
                agente = escalarTextura(new Pixmap(Gdx.files.internal("agent.png")));
                monstruo = escalarTextura(new Pixmap(Gdx.files.internal("monster.png")));
                precipicio = escalarTextura(new Pixmap(Gdx.files.internal("trap.png")));
                suelo = escalarTextura(new Pixmap(Gdx.files.internal("floor.png")));
                tesoroCerrado = escalarTextura(new Pixmap(Gdx.files.internal("chest_closed.png")));
                tesoroAbierto = escalarTextura(new Pixmap(Gdx.files.internal("chest_open.png")));
                break;
            case CONFIG2:
                if (estado != Estado.CONFIG2) {
                    botonMonstruo.setDisabled(false);
                    botonPrecipicio.setDisabled(false);
                }
                break;
            case JUEGO:
                this.cueva.agente.setFlechas(this.cueva.getMonstruos());
                this.cueva.agente.setTesoros(this.cueva.getTesoros());
                botonTesoro.setDisabled(true);
                botonMonstruo.setDisabled(true);
                botonPrecipicio.setDisabled(true);
                break;
        }
        estado = nuevo;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(241f / 255f, 230f / 255f, 174f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camara.update();
        game.batch.setProjectionMatrix(camara.combined);
        //dibujar el mapa y todos los elementos
        game.batch.begin();
        Cuadro cuadro;
        for (int i = 0; i < cueva.getSize(); i++) {
            for (int j = 0; j < cueva.getSize(); j++) {
                int x = j * cueva.costado + OFFSET_LEFT;
                int y = i * cueva.costado;
                //suelo
                game.batch.draw(suelo, x, y);
                cuadro = cueva.cuadros[i][j];
                //precipicio
                if (cuadro.isPrecipicio()) game.batch.draw(precipicio, x, y);
                //monstruo
                if (cuadro.isMonstruo()) game.batch.draw(monstruo, x, y);
                //tesoro
                if (cuadro.isTesoro()) {
                    game.batch.draw(cueva.isTesoroEncontrado() ? tesoroAbierto : tesoroCerrado, x, y);
                }
                //agente
                if (cuadro.isAgente()) game.batch.draw(agente, x, y);
                //hedor y brisa
                if (cuadro.isHedor() && cuadro.isBrisa()) {
                    //escribir HB
                    game.font.draw(game.batch, "HB", x + 2, y + 12);
                } else if (cuadro.isHedor()) {
                    //escribir H
                    game.font.draw(game.batch, "H", x + 2, y + 12);
                } else if (cuadro.isBrisa()) {
                    //escribir B
                    game.font.draw(game.batch, "B", x + 2, y + 12);
                }
            }
        }
        game.batch.end();
        //procesar input de usuario
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.changeScreen(CuevaDelMonstruo.MENU);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            autoPlay = !autoPlay;
        }
        if (!cueva.haTerminado() &&
                ((autoPlay && TimeUtils.nanoTime() - lastMoveTime > 500000000)
                        || (!autoPlay && Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)))) {
            cueva.enviarPercepciones();
            cueva.registrarAcciones();
            lastMoveTime = TimeUtils.nanoTime();
        }
        //interfaz
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Escala una imagen al tamaño de celda que se necesita para mostrar la demo correctamente
     *
     * @param original Pixmap que se quiere escalar y convertir en Texture
     * @return Texture que contiene la imagen escalada
     */
    @org.jetbrains.annotations.NotNull
    private Texture escalarTextura(Pixmap original) {
        int w = CuevaDelMonstruo.WIDTH;
        int h = CuevaDelMonstruo.HEIGHT;
        int size = cueva.getSize();
        Pixmap escalada = new Pixmap(w / size, h / size, original.getFormat());
        escalada.drawPixmap(original,
                0, 0, original.getWidth(), original.getHeight(),
                0, 0, escalada.getWidth(), escalada.getHeight());
        Texture textura = new Texture(escalada);
        escalada.dispose();
        original.dispose();
        return textura;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        agente.dispose();
        monstruo.dispose();
        precipicio.dispose();
        suelo.dispose();
        tesoroCerrado.dispose();
    }

    private enum Estado {
        CONFIG1, CONFIG2, JUEGO
    }

    private enum ModoConfig {
        NADA, TESORO, MONSTRUO, PRECIPICIO
    }
}