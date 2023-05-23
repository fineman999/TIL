pragma solidity >=0.7.0 <0.9.0;

contract ParentContract {
    function foo() public virtual returns (string memory) {
        return "Parent";
    }
}

contract ChildContract is ParentContract {
    function foo() public virtual override returns (string memory) {
        return "Child";
    }
}