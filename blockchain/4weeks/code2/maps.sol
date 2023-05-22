// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract MyContract {
    mapping(uint256 => string) public myMapping;
    uint256[] public keys;

    function setValue(uint256 key, string memory value) public {
        myMapping[key] = value;
        keys.push(key);
    }

    function getAllValues() public view returns (string[] memory) {
        string[] memory values = new string[](keys.length);
        for (uint256 i = 0; i < keys.length; i++) {
            values[i] = myMapping[keys[i]];
        }
        return values;
    }
}