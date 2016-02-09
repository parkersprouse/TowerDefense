package cs.games.td.desktop;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class CreateTexturePack {

	public static void main (String[] args) throws Exception {
        //TexturePacker.process("tobepacked", "packed", "assets.atlas");
        
        // For menu system
        // TexturePacker.process("tobepackedwin", "packedwin", "assets.atlas");
        
        // For map tileset
		TexturePacker.process("tobepacked", "packed", "assets.atlas");
    }

}