// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;

contract Monitor {

    string public name;

    constructor(string memory _name){
        name = _name;
    }

    function show() public pure returns(string memory){
        return "show";
    }
}
contract SystemUnit {
    string public name2 = "XG12";
    function turnOn() public pure returns(string memory){
        return "turnOn";
    }
}

contract MyContract {
    Monitor public monitor;
    SystemUnit public systemUnit;
    string[] private names;
    constructor() {
        monitor = new Monitor("DW12");
        systemUnit = new SystemUnit();
        addName(monitor.name());
        addName(systemUnit.name2());

    }
    function addName(string memory _name) private  {
        names.push(_name);
    }
    function getAllNames() public view returns (string[] memory) {
        return names;
    }

    function start() public view returns (string[] memory) {
        string[] memory values = new string[](2);
        values[0] = monitor.show();
        values[1] = systemUnit.turnOn();
        return values;
    }

}
