// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;
contract map {
    mapping(address => uint) public a;
    function addMapping(address _key, uint _value) public {
        a[_key] = _value;
    }
    //    데이터를 보는건 좋으나 고칠 수 없어
    function getMapping(address _key) public view returns(uint) {
        return a[_key];
    }
    function deleteMapping(address _key) public {
        //        둘 다 같은거
        delete(a[_key]);
        //        a[_key]=0;
    }
    function ChangeMapping(address _key, uint _value) public {
        a[_key] = _value;
    }

    function addBufferMapping(address _key, uint _value) public {
        a[_key] += _value;
    }

    function fun1(address _key, uint sendMoney) public  returns(uint) {
        for(uint i = 0; i < 5; i++) {
            addBufferMapping(_key, sendMoney);
        }
        return getMapping(_key);
    }

}
