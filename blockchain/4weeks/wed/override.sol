// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;
contract Student {
    string private schoolName = "Solidity University";
    string internal schoolNumbers = "02-1234-5678";

    function getSchoolName() public virtual returns(string memory) {
        return schoolName;
    }
}
contract ArtStudent is Student {

    function getSchoolName() public virtual override view returns(string memory){
        return getSchoolName();
    }
    function getSchoolNumbers() public view returns(string memory){
        return schoolNumbers;
    }
}