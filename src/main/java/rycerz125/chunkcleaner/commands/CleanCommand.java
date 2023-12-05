package rycerz125.chunkcleaner.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import rycerz125.chunkcleaner.ChunkCleaner;
import rycerz125.chunkcleaner.data.WorldDataService;

import java.io.File;

public class CleanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        File selectionFile;
        if(args.length == 1) {
            selectionFile = new File(WorldDataService.DATA_PATH + args[0] + File.separator + args[0]+".csv");
            if(selectionFile.exists()){
                if(Bukkit.getWorld(args[0]) != null){
                    sender.sendMessage(args[0]+" != null, type /mv unload");
                    return true;
                }

                long startTime = System.currentTimeMillis();
                new Thread(() ->{
                    long deletedInfo = ChunkCleaner.chunkRemover.removeChunksFromWorld(selectionFile,args[0]);
                    long regions = deletedInfo /1000000000L;
                    long chunks = deletedInfo % 1000000000L;
                    sender.sendMessage("Usunieto: "+deletedInfo+" ( " + regions  +" regions, " + chunks+ " chunks");
                    sender.sendMessage("Operacja zajela " + (System.currentTimeMillis() - startTime) + " ms");
                }).start();
            }
            else {
                sender.sendMessage("Brak pliku " + args[0] + ".csv");
            }
            return true;
        }

        return false;
    }
}
