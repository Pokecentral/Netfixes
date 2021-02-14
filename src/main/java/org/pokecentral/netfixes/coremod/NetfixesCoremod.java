package org.pokecentral.netfixes.coremod;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.pokecentral.netfixes.coremod.patches.ClickWindow;
import org.pokecentral.netfixes.coremod.patches.CustomPayload;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"org.pokecentral.netfixes.coremod"})
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class NetfixesCoremod implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                ClickWindow.class.getName(),
                CustomPayload.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
