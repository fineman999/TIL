// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;

contract Calculator{

    function types(uint param) public pure returns (uint) {
        return param;
    }

    function types(string memory param) public pure returns (string memory) {
        return param;
    }

}


contract Ex6_9 {
    Calculator internal calculator = new Calculator();
    function getValues() public view returns (uint, string memory) {
        return (calculator.types(4), calculator.types("hi"));
    }
}
