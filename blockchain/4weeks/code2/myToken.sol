// SPDX-License-Identifier: GPL-3.0

pragma solidity >=0.7.0 <0.9.0;

contract myToken {

    event _transfer(address from, address to, uint amount);

    string private tokenName; // Ether
    string private tokenSymbol; // ETH
    uint private tokenTotalSupply; // 1000
    uint private tokenDecimal; // 1 ether = 10 ^18 wei =>18

    mapping(address => uint) private balances;


    function start(string memory _name, string memory _symbol, uint _totalSupply, uint _decimal) public{
        tokenName = _name;
        tokenSymbol = _symbol;
        tokenDecimal = _decimal;
        mint(msg.sender,_totalSupply);
    }

    function name() public view returns(string memory) {
        return tokenName;
    }

    function symbol() public view returns(string memory) {
        return tokenSymbol;
    }

    function totalSupply() public view returns(uint) {
        return tokenTotalSupply;
    }

    function decimal() public view returns(uint) {
        return tokenDecimal;
    }

    function balanceOf(address _addr) public view returns(uint) {
        return balances[_addr];
    }

    function mint(address _addr, uint amount) internal virtual {
        balances[_addr] += amount;
        tokenTotalSupply += amount;
    }

    function transfer(address _to, uint _amount) public {
        require( balances[msg.sender] >= _amount, "Too much to send tokens");
        balances[msg.sender] -= _amount;
        balances[_to] += _amount;

        emit _transfer(msg.sender, _to, _amount);
    }
}