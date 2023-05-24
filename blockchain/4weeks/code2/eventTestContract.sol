// SPDX-License-Identifier: MIT
pragma solidity ^0.8.6;

contract EventTestContract {

    mapping(address => uint256) private _counts;

    event Increase(address indexed addr, uint256 oldValue, uint256 newValue);

    function increaseWithEvent() public returns (bool) {
        uint256 oldValue = _counts[msg.sender];
        _increase();
        uint256 newValue = _counts[msg.sender];
        emit Increase(msg.sender, oldValue, newValue);
        return true;
    }

    function increaseWithoutEvent() public returns (bool) {
        _increase();
        return true;
    }

    function _increase() private {
        _counts[msg.sender] += 1;
    }

    function count() public view returns (uint256) {
        return _counts[msg.sender];
    }

}