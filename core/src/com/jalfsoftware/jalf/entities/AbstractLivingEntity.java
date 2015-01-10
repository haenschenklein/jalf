package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.jalfsoftware.jalf.helper.Map;
import com.jalfsoftware.jalf.screens.GameScreen;

/**
 * Erweitert AbstractEntity um Lebenspunkte, Beschleunigung, Maximalgeschwindigkeit, Bewegung (inklusive Kollisionsabfrage)
 */
public abstract class AbstractLivingEntity extends AbstractEntity {
    public static final String LOG = AbstractLivingEntity.class.getSimpleName();

    protected int currentHealth;
    protected int maxHealth;
    protected int maxJumps = 2;

    protected float   acceleration;
    protected float   maxSpeed;
    protected float   jumpSpeed;
    protected Vector2 currentSpeed;
    protected Boolean fireballAvalible;

    private int jumpCount;

    private Direction           requestedDirection;
    protected Direction           lastXDirection;
    private LadderDirection     requestedLadderDirection;
    private Map.TilePhysicsType currentAppliedPhysics;
    private boolean             entityIsOnLadder;
    private boolean             entityIsMidAir;

    public AbstractLivingEntity(float xPos, float yPos, String Regionname, int currentHealth, int maxHealth, float acceleration,
                                float maxSpeed, float jumpSpeed, GameScreen gameScreen) {
        super(xPos, yPos, Regionname, gameScreen);
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;

        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.jumpSpeed = jumpSpeed;
        this.fireballAvalible = false;

        jumpCount = 0;
        currentSpeed = new Vector2(0, 0);
        entityIsOnLadder = false;
        entityIsMidAir = false;

        requestedDirection = Direction.NONE;
        lastXDirection = Direction.NONE;
        requestedLadderDirection = LadderDirection.NONE;
        currentAppliedPhysics = Map.TilePhysicsType.NONE;
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);

        float delta = Gdx.graphics.getDeltaTime();
        updateCurrentSpeed(delta);
        doCollisionDetectionHorizontal();
        doCollisionDetectionVertical();
        doOutOfMapDetectionBottom();
        doItemDetection();
        updateLadderFlag();
        updateLastXDirection();
    }

    private void updateLastXDirection() {
        if (currentSpeed.x > 0) lastXDirection = Direction.RIGHT;
        else if (currentSpeed.x < 0) lastXDirection = Direction.LEFT;
    }

    /**
     * Entity verliert Leben, wenn sie aus der Map fällt
     */
    private void doOutOfMapDetectionBottom() {
        if (getY() < -getEntityHeight()) takeDamage(maxHealth);
    }

    /**
     * Updated currentSpeed unter Verwendung von requestedDirection, currentAppliedPhysics, acceleration, maxSpeed und Gravitation
     */
    private void updateCurrentSpeed(float delta) {
        float actualAcceleration;
        float actualMaxSpeed;

        // TilePhysics
        updateCurrentAppliedPhysics();
        switch (currentAppliedPhysics) {
            case ICE:
                actualAcceleration = Map.TILE_ICE_ACCELERATION;
                actualMaxSpeed = Map.TILE_ICE_MAX_SPEED;
                break;
            case MUD:
                actualAcceleration = Map.TILE_MUD_ACCELERATION;
                actualMaxSpeed = Map.TILE_MUD_MAX_SPEED;
                break;
            default:
                actualAcceleration = acceleration;
                actualMaxSpeed = maxSpeed;
        }

        // X-Achse
        // Beschleunigen
        switch (requestedDirection) {
            case LEFT:
                if (!entityIsMidAir || currentSpeed.x > -maxSpeed) {
                    currentSpeed.x -= actualAcceleration * delta;
                    if (currentSpeed.x < -actualMaxSpeed) currentSpeed.x = -actualMaxSpeed;
                }
                break;
            case RIGHT:
                if (!entityIsMidAir || currentSpeed.x < maxSpeed) {
                    currentSpeed.x += actualAcceleration * delta;
                    if (currentSpeed.x > actualMaxSpeed) currentSpeed.x = actualMaxSpeed;
                }
                break;
        }

        // Verlangsamen
        if (requestedDirection == Direction.NONE && Math.abs(currentSpeed.x) > 0) {
            if (currentSpeed.x > 0) {
                currentSpeed.x -= actualAcceleration * delta;
                if (currentSpeed.x < 0) currentSpeed.x = 0;
            } else {
                currentSpeed.x += actualAcceleration * delta;
                if (currentSpeed.x > 0) currentSpeed.x = 0;
            }
        }

        // Y-Achse
        // Beschleunigen (Gravitation)
        currentSpeed.y -= GameScreen.GRAVITATION_CONSTANT;
        if (entityIsOnLadder) {
            switch (requestedLadderDirection) {
                case UP:
                    currentSpeed.y = Map.TILE_LADDER_SPEED;
                    break;
                case DOWN:
                    currentSpeed.y = -Map.TILE_LADDER_SPEED;
                    break;
                default:
                    currentSpeed.y = 0;
            }
            currentAppliedPhysics = Map.TilePhysicsType.NONE;
        }
    }

    /**
     * Updated das Feld currentAppliedPhysics, wenn die Entity auf einem Tile steht auf Basis dessen Physikeigenschaften
     */
    private void updateCurrentAppliedPhysics() {
        Vector2 mapPosition = gameScreen.getMap().convertToMapPosition(new Vector2(getX() + getEntityWidth() / 2, getY() - 1));
        Map.TilePhysicsType tilePhysicsType = gameScreen.getMap().isTilePhysicsTile((int) mapPosition.x, (int) mapPosition.y);
        if (gameScreen.getMap().isPositionBlocked((int) mapPosition.x, (int) mapPosition.y)) {
            currentAppliedPhysics = tilePhysicsType;
            entityIsMidAir = false;
        } else entityIsMidAir = true;
    }

    /**
     * Updated entityIsOnLadder
     */
    private void updateLadderFlag() {
        float mapHeadYPos = gameScreen.getMap().convertToMapUnits(getY() + getEntityHeight() - 1);
        float mapFeetYPos = gameScreen.getMap().convertToMapUnits(getY());
        float leftAdjustedXPos = gameScreen.getMap().convertToMapUnits(getX() + getEntityWidth() * 0.25f);
        float rightAdjustedXPos = gameScreen.getMap().convertToMapUnits(getX() + getEntityWidth() * 0.75f);

        entityIsOnLadder = gameScreen.getMap().isTileLadderTile((int) leftAdjustedXPos, (int) mapFeetYPos) ||
                           gameScreen.getMap().isTileLadderTile((int) leftAdjustedXPos, (int) mapHeadYPos) ||
                           gameScreen.getMap().isTileLadderTile((int) rightAdjustedXPos, (int) mapFeetYPos) ||
                           gameScreen.getMap().isTileLadderTile((int) rightAdjustedXPos, (int) mapHeadYPos);
    }

    /**
     * Updated Position und currentSpeed bei Kollision der Entität mit Tiles der Map in der X-Achse
     */
    private void doCollisionDetectionHorizontal() {
        boolean blockedX = false;

        float entityMapWidth = gameScreen.getMap().convertToMapUnits(getEntityWidth());

        int newMapPositionX = (int) gameScreen.getMap().convertToMapUnits(getX() + currentSpeed.x);

        // Berührende Tilehöhen an Kanten finden
        // Unten
        int yPosTileBottom = (int) gameScreen.getMap().convertToMapUnits(getY());
        // Oben
        int yPosTileTop = (int) gameScreen.getMap().convertToMapUnits(getY() + getEntityHeight() - 1);

        if (currentSpeed.x > 0) {
            if ((getX() + getEntityWidth() + currentSpeed.x) > gameScreen.getMap().getMapWidthAsScreenUnits()) {
                blockedX = true;
            } else for (int i = yPosTileBottom; i <= yPosTileTop; i++) {
                blockedX = gameScreen.getMap().isPositionBlocked((int) (newMapPositionX + entityMapWidth), i);
                if (blockedX) break;
            }
        } else if (currentSpeed.x < 0) {
            if ((getX() + currentSpeed.x) < 0) {
                blockedX = true;
            } else for (int i = yPosTileBottom; i <= yPosTileTop; i++) {
                blockedX = gameScreen.getMap().isPositionBlocked(newMapPositionX, i);
                if (blockedX) break;
            }
        } else {
            blockedX = false;
        }

        if (blockedX) {
            int mapX = (int) gameScreen.getMap().convertToMapUnits(getX() + ((currentSpeed.x > 0) ? 1 : 0) * (getEntityWidth() - 1));
            setX((int) gameScreen.getMap().convertToScreenUnits(mapX));
            currentSpeed.x = 0;
        }

        // Neue X-Position setzen
        setX(getX() + currentSpeed.x);
    }

    /**
     * Updated Position und currentSpeed bei Kollision der Entität mit Tiles der Map in der Y-Achse
     */
    private void doCollisionDetectionVertical() {
        boolean blockedYBottom, blockedYTop, blockedYLadder;

        int entityMapHeight = (int) gameScreen.getMap().convertToMapUnits(getEntityHeight());

        float newMapPositionY = gameScreen.getMap().convertToMapUnits(getY() + currentSpeed.y);
        float newMapPositionX = gameScreen.getMap().convertToMapUnits(getX());
        float newMapPositionXRight = gameScreen.getMap().convertToMapUnits(getX() + getEntityWidth() - 1);

        // Unten
        blockedYBottom = gameScreen.getMap().isPositionBlocked((int) newMapPositionX, (int) newMapPositionY) ||
                         gameScreen.getMap().isPositionBlocked((int) newMapPositionXRight, (int) newMapPositionY);

        blockedYLadder =
                gameScreen.getMap().isTileLadderTile((int) gameScreen.getMap().convertToMapUnits(getX() + getEntityWidth() * 0.25f),
                                                     (int) newMapPositionY) ||
                gameScreen.getMap().isTileLadderTile((int) gameScreen.getMap().convertToMapUnits(getX() + getEntityWidth() * 0.75f),
                                                     (int) newMapPositionY);
        // Oben
        blockedYTop = gameScreen.getMap().isPositionBlocked((int) newMapPositionX, (int) newMapPositionY + entityMapHeight) ||
                      gameScreen.getMap().isPositionBlocked((int) newMapPositionXRight, (int) newMapPositionY + entityMapHeight);

        if (blockedYBottom) {
            int mapY = (int) gameScreen.getMap().convertToMapUnits(getY());
            setY((int) gameScreen.getMap().convertToScreenUnits(mapY));

            currentSpeed.y = 0;
            jumpCount = 0;
        } else if (blockedYLadder && !entityIsOnLadder && requestedLadderDirection != LadderDirection.DOWN) {
            // Nur teleportieren, wenn auf dem oberen Ende der Leiter gelandet -> nicht beim seitlichen Reinspringen
            if (!gameScreen.getMap().isTileLadderTile((int) gameScreen.getMap().convertToMapUnits(getX() + getEntityWidth() * 0.25f),
                                                      (int) gameScreen.getMap().convertToMapUnits(getY())) &&
                !gameScreen.getMap().isTileLadderTile((int) gameScreen.getMap().convertToMapUnits(getX() + getEntityWidth() * 0.75f),
                                                      (int) gameScreen.getMap().convertToMapUnits(getY()))) {
                int mapY = (int) gameScreen.getMap().convertToMapUnits(getY());
                setY((int) gameScreen.getMap().convertToScreenUnits(mapY));
            }

            currentSpeed.y = 0;
            jumpCount = 0;
        }
        if (blockedYTop) {
            int mapY = (int) gameScreen.getMap().convertToMapUnits(getY() + (getEntityHeight() / 3 - 1));
            setY((int) gameScreen.getMap().convertToScreenUnits(mapY));

            currentSpeed.y = 0;
        }

        // Neue Y-Position setzen
        setY(getY() + currentSpeed.y);
    }


    private void doItemDetection() {
        //Rectangle player = new Rectangle();
        float entityLeftBotX = gameScreen.getMap().convertToMapUnits(getX());
        float entityLeftBotY = gameScreen.getMap().convertToMapUnits(getY());
        float entityHeight = gameScreen.getMap().convertToMapUnits(getEntityHeight());
        float entityWidth = gameScreen.getMap().convertToMapUnits(getEntityWidth());
        String item = null;
        Vector2 itemPosition = new Vector2();

        for (float x = entityLeftBotX; x <= entityLeftBotX + entityWidth; x++) {
            for (float y = entityLeftBotY; y <= entityLeftBotY + entityHeight; y++) {
                if (gameScreen.getMap().isPositionItemPosition((int) x, (int) y) != null) {
                    item = gameScreen.getMap().isPositionItemPosition((int) x, (int) y);
                    itemPosition.set(x, y);
                    break;
                }
                //System.out.println("coords: " + x + " " + y);
            }
        }


        if (item != null) {
            System.out.println("item detected! " + item.toString());
            System.out.println("---");
            // remove Tile
            if (gameScreen.getMap().getForegroundLayerCell((int) itemPosition.x, (int) itemPosition.y) != null)
                gameScreen.getMap().getForegroundLayerCell((int) itemPosition.x, (int) itemPosition.y).setTile(null);

            switch (item) {
                case "speed":
                    itemSpeedBoost();
                    break;
                case "jump":
                    itemJumpBoost();
                    break;
                case "fireball":
                    itemSetFireballAvalible();
                    break;
                case "hp":
                    itemHpPlus();
                    break;
                case "live":
                    itemLivePlus();
                    break;
                case "coin":
                    itemCoinPlus();
                    break;
                default:
                    break;
            }
        }
    }


    protected abstract void itemCoinPlus();

    protected abstract void itemSetFireballAvalible();

	protected abstract void itemJumpBoost();

	protected abstract void itemSpeedBoost();

	protected abstract void itemLivePlus();

    protected abstract void itemHpPlus();

	public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    protected void move(Direction direction) {
        requestedDirection = direction;
    }

    protected void moveLadder(LadderDirection direction) {
        requestedLadderDirection = direction;
    }

    protected void jump() {
        if (jumpCount < maxJumps) {
            jumpCount++;
            currentSpeed.y = jumpSpeed;
        }
    }

    /**
     * Setzt die aktuelle Geschwindigkeit in beiden Achsen auf 0
     */
    protected void resetSpeed() {
        currentSpeed.x = currentSpeed.y = 0;
    }

    public float getEntityHeight() {
        return sprite.getHeight();
    }

    public float getEntityWidth() {
        return sprite.getWidth();
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isAlive() {
        return (currentHealth > 0);
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getMaxJumps() {
        return maxJumps;
    }

    public void setMaxJumps(int maxJumps) {
        this.maxJumps = maxJumps;
    }

    public float getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(float jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public Vector2 getCurrentSpeed() {
        return currentSpeed;
    }

    public Direction getRequestedDirection() {
        return requestedDirection;
    }

    public static enum Direction {
        LEFT,
        RIGHT,
        NONE
    }

    public static enum LadderDirection {
        UP,
        DOWN,
        NONE
    }

    public Vector2 getPosition(){
        return new Vector2(sprite.getX(), sprite.getY());
    }
}
