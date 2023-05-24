// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;
contract Number {
    uint private num = 4;
    function setNum(uint _num) public {
        num = _num;
    }

    function getNum() public view returns (uint){
        return num;
    }

}

contract Caller{

    Number internal insance = new Number();

    function changeNumber() public {
        insance.setNum(5);
    }

    function getNumber() public view returns (uint) {
        return insance.getNum();
    }

}