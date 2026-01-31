package cc.ayakurayuki.nginxconfigurationformatter

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey
import java.util.function.Supplier

@NonNls
private const val BUNDLE = "messages.I18NMessageBundle"

/**
 * @author Ayakura Yuki
 * @date 2026/01/31-23:30
 */
internal object I18NMessageBundle {

    private val INSTANCE = DynamicBundle(I18NMessageBundle::class.java, BUNDLE)

    fun message(
        key: @PropertyKey(resourceBundle = BUNDLE) String,
        vararg params: Any
    ): @Nls String {
        return INSTANCE.getMessage(key, *params)
    }

    fun lazyMessage(
        key: @PropertyKey(resourceBundle = BUNDLE) String,
        vararg params: Any
    ): Supplier<@Nls String> {
        return INSTANCE.getLazyMessage(key, *params)
    }

}
