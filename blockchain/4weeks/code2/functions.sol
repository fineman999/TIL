// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract exeam2 {
    uint public var1 = 1;
    uint var2 = 2; // 화면상에 나오지 않음
    uint internal var3 = 3; // 화면상에 미출력

    function fun_public() public view returns (uint, uint, uint) {
        return (var1, var2, var3);
    }
    function fun_private() private view returns (uint, uint, uint) {
        return (var1, var2, var3);
    }
    function fun_external() external view returns (uint, uint, uint) {
        return (var1, var2, var3);
    }
    function fun_internal() internal view returns (uint, uint, uint) {
        return (var1, var2, var3);
    }

}
