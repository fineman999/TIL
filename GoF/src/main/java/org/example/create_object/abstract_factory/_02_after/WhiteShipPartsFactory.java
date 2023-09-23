package org.example.create_object.abstract_factory._02_after;

import org.example.create_object.abstract_factory._01_before.WhiteAnchor;
import org.example.create_object.abstract_factory._01_before.WhiteCabin;

public class WhiteShipPartsFactory implements ShipPartsFactory {
    @Override
    public Cabin createCabin() {
        return new WhiteCabin();
    }

    @Override
    public Anchor createAnchor() {
        return new WhiteAnchor();
    }
}
