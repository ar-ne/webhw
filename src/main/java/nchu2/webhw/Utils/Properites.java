package nchu2.webhw.Utils;

public class Properites {
    public static class Tamplet {
        public static final String Customer = "Customer";
        public static final String Manager = "Manager";
        public static final String Staff = "Staff";
    }

    public static class UserTyle {
        public static final int Customer = 0;
        public static final int Manager = 1;
        public static final int Staff = 2;
    }

    //    #show显示 #hide隐藏 #editable可编辑 #uneditable不可编辑 #direct直接显示(SEX=1转为SEX=男) #indirect编码后显示
    public static class FieldFlag {
        public static class Visibility {
            public static final String S = "SHOW";
            public static final String H = "HIDE";
        }

        public static class Editability {
            public static final String E = "EDITABLE";
            public static final String U = "UNEDITABLE";
        }

        public static class ShowType {
            public static final String D = "DIRECT";
            public static final String I = "INDIRECT";
        }

    }
}
