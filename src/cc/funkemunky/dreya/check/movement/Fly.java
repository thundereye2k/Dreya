package cc.funkemunky.dreya.check.movement;

import cc.funkemunky.dreya.Dreya;
import cc.funkemunky.dreya.check.Check;
import cc.funkemunky.dreya.check.CheckType;
import cc.funkemunky.dreya.data.PlayerData;
import cc.funkemunky.dreya.util.MathUtils;
import cc.funkemunky.dreya.util.PlayerUtils;
import cc.funkemunky.dreya.util.SetBackSystem;
import cc.funkemunky.dreya.util.VelocityUtils;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Created by Mr_JaVa_ on 2018-04-08 Package cc.funkemunky.dreya.check.movement
 */
public class Fly extends Check {
    public Fly() {
        super("Flight", CheckType.MOVEMENT, true);
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.CREATIVE)
                || p.getAllowFlight()
                || e.getPlayer().getVehicle() != null
                || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE) {
            return;
        }

        PlayerData data = Dreya.getInstance().getDataManager().getData(p);

        if(data == null) {
            return;
        }

        //Ascension Check
        Vector vec = new Vector(to.getX(), to.getY(), to.getZ());
        double Distance = vec.distance(new Vector(from.getX(),from.getY(),from.getZ()));
        if (p.getFallDistance() == 0.0f && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR && p.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
            if (Distance > 0.50 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                flag(p,"Type: A [1]");
                setBackPlayer(p);
            } else if (Distance > 0.90 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                flag(p,"Type: A [2]");
                setBackPlayer(p);
            } else if (Distance > 1.0 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                flag(p,"Type: A [3]");
                setBackPlayer(p);
            } else if (Distance > 3.24 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                flag(p,"Type: A [4]");
                setBackPlayer(p);
            }
        }
        if (e.getTo().getY() > e.getFrom().getY() && data.getAirTicks() > 2) {
            if (!PlayerUtils.isOnGround3(p) && !PlayerUtils.onGround2(p) && !PlayerUtils.isOnGround(p)) {
                if (PlayerUtils.getDistanceToGround(p) > 2) {
                    if (data.getGoingUp_Blocks() >= 3) {
                        flag(p,"Type: A [5]");
                    } else {
                        data.setGoingUp_Blocks(data.getGoingUp_Blocks() + 1);
                    }
                } else {
                    data.setGoingUp_Blocks(0);
                }
            } else {
                data.setGoingUp_Blocks(0);
            }
        } else if (e.getTo().getY() < e.getFrom().getY()) {
            data.setGoingUp_Blocks(0);
        } else {
            data.setGoingUp_Blocks(0);
        }
        //Hover check
        if(!PlayerUtils.isOnGround(p)) {
            double distanceToGround = getDistanceToGround(p);
            double yDiff = MathUtils.getVerticalDistance(e.getFrom(), e.getTo());
            int verbose = data.getFlyHoverVerbose();

            if(distanceToGround > 2) {
                verbose = yDiff == 0 ? verbose + 6 : yDiff < 0.06 ? verbose + 4 : 0;
            } else if(data.getAirTicks() > 7
                    && yDiff == 0) {
                verbose+= 2;
            } else {
                verbose = 0;
            }

            if(verbose > 17) {
                flag(p, "Type: B");
                setBackPlayer(p);
                verbose = 0;
            }
            data.setFlyHoverVerbose(verbose);
        }

        //Too Fast Check
        if (VelocityUtils.didTakeVelocity(p)) {
            return;
        }
    //    p.sendMessage(""+p.getFlySpeed());
        if (e.getTo() != e.getFrom()) {

        }
    }
    @EventHandler
    public void onKickEvent(PlayerKickEvent e) {
        if (e.getReason().equalsIgnoreCase("Flying is not enabled on this server")) {
            e.setCancelled(true);
            flag(e.getPlayer(),"Type: C");
            setBackPlayer(e.getPlayer());
        }
    }

    private int getDistanceToGround(Player p){
        Location loc = p.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--){
            loc.setY(i);
            if(loc.getBlock().getType().isSolid() || loc.getBlock().isLiquid())break;
            distance++;
        }
        return distance;
    }
    private static void setBackPlayer(Player p) {
        SetBackSystem.setBack(p);
    }
}
