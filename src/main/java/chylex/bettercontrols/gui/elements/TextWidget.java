package chylex.bettercontrols.gui.elements;
import chylex.bettercontrols.gui.OptionListWidget.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import java.util.List;

public final class TextWidget extends DrawableHelper implements Widget{
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	
	private final Text text;
	private int x;
	private int y;
	private final int width;
	private final int height;
	private final int align;
	
	public TextWidget(final int x, final int y, final int width, final int height, final Text text, final int align){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.align = align;
	}
	
	public TextWidget(final int x, final int y, final int width, final Text text, final int align){
		this(x, y, width, 20, text, align);
	}
	
	public TextWidget(final int x, final int y, final int width, final Text text){
		this(x, y, width, 20, text, LEFT);
	}
	
	@Override
	public int getX(){
		return x;
	}
	
	@Override
	public int getY(){
		return y;
	}
	
	@Override
	public void setX(final int x){
		this.x = x;
	}
	
	@Override
	public void setY(final int y){
		this.y = y;
	}
	
	@Override
	public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta){
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		final List<OrderedText> lines = textRenderer.wrapLines(text, width);
		final int lineHeight = textRenderer.fontHeight + 1;
		
		final int finalX = align == CENTER ? x + (width / 2) - (lines.stream().mapToInt(textRenderer::getWidth).max().orElse(0) / 2) : x;
		final int finalY = y + (height / 2) - (lineHeight * lines.size() / 2) + 1;
		
		for(int i = 0; i < lines.size(); i++){
			final OrderedText line = lines.get(i);
			textRenderer.drawWithShadow(matrices, line, finalX, finalY + (i * lineHeight), (255 << 16) | (255 << 8) | 255);
		}
	}
}
