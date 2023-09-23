package org.example.create_object.abstract_factory._02_after;

public class WhiteProProFactory implements ShipPartsFactory {
    @Override
    public Anchor createAnchor() {
        return new WhiteAnchorPro();
    }

    @Override
    public Cabin createCabin() {
        return new WhiteCabinPro();
    }
}
