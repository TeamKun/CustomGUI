package kotx.minecraft.plugins.customgui.extensions

import org.bukkit.ChatColor
import org.bukkit.Location

class LocationFormatter(
    private val formatter: String,
) {
    companion object {
        val LOCATION = LocationFormatter("[x, y, z]")
        val BLOCK_LOCATION = LocationFormatter("[bx, by, bz]")
        val DETAILS = LocationFormatter("[x, y, z, Yaw:yaw, Pitch:pitch, World:world]")
        val LOCATION_COLORED = LocationFormatter(
            "[".format(ChatColor.GRAY) +
                    "x".format(ChatColor.RED) +
                    ", ".format(ChatColor.DARK_GRAY) +
                    "y".format(ChatColor.RED) +
                    ", ".format(ChatColor.DARK_GRAY) +
                    "z".format(ChatColor.RED) +
                    "]".format(ChatColor.GRAY)
        )
        val BLOCK_LOCATION_COLORED = LocationFormatter(
            "[".format(ChatColor.GRAY) +
                    "bx".format(ChatColor.RED) +
                    ", ".format(ChatColor.DARK_GRAY) +
                    "by".format(ChatColor.RED) +
                    ", ".format(ChatColor.DARK_GRAY) +
                    "bz".format(ChatColor.RED) +
                    "]".format(ChatColor.GRAY)
        )
        val DETAILS_COLORED = LocationFormatter(
            "[".format(ChatColor.GRAY) +
                    "x".format(ChatColor.RED) +
                    ", ".format(ChatColor.DARK_GRAY) +
                    "y".format(ChatColor.RED) +
                    ", ".format(ChatColor.DARK_GRAY) +
                    "z".format(ChatColor.RED) +
                    ", Yaw:".format(ChatColor.DARK_GRAY) +
                    "yaw".format(ChatColor.RED) +
                    ", Pitch:".format(ChatColor.DARK_GRAY) +
                    "pitch".format(ChatColor.RED) +
                    ", World:".format(ChatColor.DARK_GRAY) +
                    "world".format(ChatColor.RED) +
                    "]".format(ChatColor.GRAY)
        )

    }

    fun replace(location: Location) = formatter.replace("bx", location.blockX.toString())
        .replace("by", location.blockY.toString())
        .replace("bz", location.blockZ.toString())
        .replace("yaw", location.yaw.toString())
        .replace("pitch", location.pitch.toString())
        .replace("world", location.world.name)
        .replace("x", location.x.toString())
        .replace("y", location.y.toString())
        .replace("z", location.z.toString())
}

fun Location.format(formatter: LocationFormatter = LocationFormatter.BLOCK_LOCATION_COLORED) = formatter.replace(this)