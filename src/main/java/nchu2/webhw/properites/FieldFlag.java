package nchu2.webhw.properites;

//    #show显示 #hide隐藏 #editable可编辑 #uneditable不可编辑 #direct直接显示(SEX=1转为SEX=男) #indirect编码后显示
public class FieldFlag {
    public static class Visibility {
        public static final boolean SHOW = true;
        public static final boolean HIDE = false;
    }

    public static class Editability {
        public static final boolean EDITABLE = true;
        public static final boolean UNEDITABLE = false;
    }

    public static class ShowType {
        public static final boolean DIRECT = true;
        public static final boolean INDIRECT = false;
    }

}
