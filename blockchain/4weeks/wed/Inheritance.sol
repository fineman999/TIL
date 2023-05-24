// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;
contract Student {
    string private schoolName = "Solidity University";
    string public major;

    constructor(string memory _major) {
        major = _major;
    }
}
contract ArtStudent is Student("Art") {

}


contract MusicStudent is Student{
    string internal  degree = "Music";
    constructor() Student(degree) {

    }

    function getDegree() public view returns (string memory) {
        return degree;
    }


}


contract MathStudent is Student {

    constructor(string memory _major) Student(_major) {

    }

}