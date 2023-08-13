package org.hello.chapter04.item15.member;

class DefaultMemberService implements MemberService {

    private String name;
    private static class PrivateStaticClass {
    }

    private class PrivateClass {
        void doPrint() {
            System.out.println(name);
        }
    }
}
