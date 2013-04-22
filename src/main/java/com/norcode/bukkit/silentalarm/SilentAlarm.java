package com.norcode.bukkit.silentalarm;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;
import org.bukkit.plugin.java.JavaPlugin;

public class SilentAlarm extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    private static final BlockFace[] sides = new BlockFace[] { BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH };
    
    @EventHandler(ignoreCancelled=true)
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[silentalarm]")) {
            if (event.getPlayer().hasPermission("silentalarm.create")) {
                event.setLine(0, ChatColor.DARK_BLUE + event.getLine(0));
            }
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onNotePlay(NotePlayEvent event) {
        Block signBlock = getAttachedSign(event.getBlock());
        if (signBlock != null) {
            Sound sound = getSound(event.getInstrument());
            float pitch = getPitch(event.getNote());
            org.bukkit.block.Sign sign = (org.bukkit.block.Sign) signBlock.getState();
            String l1 = sign.getLine(0);
            ArrayList<Player> recipients = new ArrayList<Player>();
            if (l1 != null && l1.equalsIgnoreCase(ChatColor.DARK_BLUE + "[silentalarm]")) {
                event.setCancelled(true);
                for (String line: sign.getLines()) {
                    if (!l1.equals(line) && line != null) {
                        Player p = getServer().getPlayer(line);
                        if (p != null) {
                            p.playSound(p.getLocation(), sound, 4.0f, pitch);
                        }
                    }
                }
            }
        }
    }

    public Sound getSound(Instrument i) {
        switch (i) {
        case BASS_DRUM:
            return Sound.NOTE_BASS_DRUM;
        case BASS_GUITAR:
            return Sound.NOTE_BASS_GUITAR;
        case PIANO:
            return Sound.NOTE_PIANO;
        case SNARE_DRUM:
            return Sound.NOTE_SNARE_DRUM;
        case STICKS:
            return Sound.NOTE_STICKS;
        default:
            return Sound.NOTE_PIANO;
        }
    }
    
    public float getPitch(Note note) {
        int o = note.getOctave();
        switch (note.getTone()) {
        case F:
            if (note.isSharped()) {
                switch (o) { 
                case 0: return 0.5f;
                case 1: return 1f;
                case 2: return 2f;
                }
            } else {
                switch (o) {
                case 0: return 0.943874f;
                case 1: return 1.887749f;
                }
            }
            break;
        case G:
            if (note.isSharped()) {
                switch (o) { 
                case 0: return 0.561231f;
                case 1: return 1.122462f;
                }
            } else {
                switch (o) {
                case 0: return 0.529732f;
                case 1: return 1.059463f;
                }
            }
            break;
        case A:
            if (note.isSharped()) {
                switch (o) { 
                case 0: return 0.629961f;
                case 1: return 1.259921f;
                }
            } else {
                switch (o) {
                case 0: return 0.594604f;
                case 1: return 1.189207f;
                }
            }
            break;
        case B:
            switch (o) {
                case 0: return 0.667420f;
                case 1: return 1.334840f;
            }
            break;
        case C:
            if (note.isSharped()) {
                switch (o) { 
                case 0: return 0.749154f;
                case 1: return 1.498307f;
                }
            } else {
                switch (o) {
                case 0: return 0.707107f;
                case 1: return 1.414214f;
                }
            }
            break;
        
        case D:
            if (note.isSharped()) {
                switch (o) { 
                case 0: return 0.840896f;
                case 1: return 1.681793f;
                }
            } else {
                switch (o) {
                case 0: return 0.793701f;
                case 1: return 1.587401f;
                }
            }
            break;
        case E:
            switch (o) {
            case 0: return 0.890899f;
            case 1: return 1.781797f;
            }
        }
        return -1f;
    }

    public Block getAttachedSign(Block block) {
        Block b = null;
        for (BlockFace side: sides) {
            b = block.getRelative(side);
            if (b.getType().equals(Material.WALL_SIGN)) {
                Sign sign = (Sign) b.getType().getNewData(b.getData());
                if (sign.isWallSign() && sign.getAttachedFace().equals(side.getOppositeFace())) {
                    return b;
                }
            }
        }
        return null;
    }
}
