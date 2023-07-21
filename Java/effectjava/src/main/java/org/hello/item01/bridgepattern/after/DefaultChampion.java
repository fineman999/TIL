package org.hello.item01.bridgepattern.after;

import org.hello.item01.bridgepattern.before.Champion;

public class DefaultChampion implements Champion {

    private Skin skin;

    private String name;

    public DefaultChampion(Skin skin, String name) {
        this.skin = skin;
        this.name = name;
    }

    @Override
    public void move() {
        System.out.printf("%s %s가 이동합니다.\n", skin.getSkinName(), name);
    }

    @Override
    public void skillQ() {
        System.out.printf("%s %s가 Q 스킬을 사용합니다.\n", skin.getSkinName(), name);

    }

    @Override
    public void skillW() {
        System.out.printf("%s %s가 W 스킬을 사용합니다.\n", skin.getSkinName(), name);
    }

    @Override
    public void skillE() {
        System.out.printf("%s %s가 E 스킬을 사용합니다.\n", skin.getSkinName(), name);
    }

    @Override
    public void skillR() {
        System.out.printf("%s %s가 R 스킬을 사용합니다.\n", skin.getSkinName(), name);
    }
}
