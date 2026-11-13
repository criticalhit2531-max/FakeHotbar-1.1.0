package dev.fakehotbar.screen;

import dev.fakehotbar.config.HotbarConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class HotbarOrderScreen extends Screen {
    private int[] order;
    private int selectedPosition = -1;

    public HotbarOrderScreen() {
        super(Text.literal("Fake Hotbar Settings"));
        this.order = HotbarConfig.getDisplayOrder();
    }

    @Override
    protected void init() {
        int buttonHeight = 34;
        int gap = 3;
        int buttonWidth = Math.min(34, Math.max(24, (this.width - 40 - gap * 8) / 9));
        int totalWidth = buttonWidth * 9 + gap * 8;
        int startX = (this.width - totalWidth) / 2;
        int rowY = this.height / 2 - 20;

        for (int position = 0; position < 9; position++) {
            final int currentPosition = position;
            int x = startX + position * (buttonWidth + gap);
            addDrawableChild(ButtonWidget.builder(
                    Text.literal(Integer.toString(order[position])),
                    button -> selectPosition(currentPosition)
            ).dimensions(x, rowY, buttonWidth, buttonHeight).build());
        }

        int controlsY = rowY + 52;
        addDrawableChild(ButtonWidget.builder(Text.literal("Reset"), button -> {
            order = new int[]{4, 5, 3, 1, 6, 7, 2, 8, 9};
            selectedPosition = -1;
            rebuild();
        }).dimensions(this.width / 2 - 106, controlsY, 68, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
            HotbarConfig.setDisplayOrder(order);
            close();
        }).dimensions(this.width / 2 - 34, controlsY, 68, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> close())
                .dimensions(this.width / 2 + 38, controlsY, 68, 20).build());
    }

    private void selectPosition(int position) {
        if (selectedPosition < 0) {
            selectedPosition = position;
            rebuild();
            return;
        }

        if (selectedPosition == position) {
            selectedPosition = -1;
            rebuild();
            return;
        }

        int temp = order[selectedPosition];
        order[selectedPosition] = order[position];
        order[position] = temp;
        selectedPosition = -1;
        rebuild();
    }

    private void rebuild() {
        clearChildren();
        init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 70, 0xFFFFFF);
        String help = selectedPosition < 0
                ? "Select two numbers to swap their positions"
                : "Selected position " + (selectedPosition + 1) + " - choose another";
        context.drawCenteredTextWithShadow(textRenderer, help, width / 2, height / 2 - 48, 0xAAAAAA);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
