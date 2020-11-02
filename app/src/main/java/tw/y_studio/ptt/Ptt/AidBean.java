package tw.y_studio.ptt.Ptt;

public class AidBean {
    // 看板名稱
    private String boardTitle;

    // 文章編號
    private String aid;

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
        if (boardTitle.equals("iPhone")) {
            this.boardTitle = "iOS";
        }
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public static class Builder {
        private String boardTitle;
        private String aid;

        private Builder() {}

        public static Builder anAidBean() {
            return new Builder();
        }

        public Builder withBoardTitle(String boardTitle) {
            this.boardTitle = boardTitle;
            return this;
        }

        public Builder withAid(String aid) {
            this.aid = aid;
            return this;
        }

        public Builder but() {
            return anAidBean().withBoardTitle(boardTitle).withAid(aid);
        }

        public AidBean build() {
            AidBean aidBean = new AidBean();
            aidBean.setBoardTitle(boardTitle);
            aidBean.setAid(aid);
            return aidBean;
        }
    }
}
