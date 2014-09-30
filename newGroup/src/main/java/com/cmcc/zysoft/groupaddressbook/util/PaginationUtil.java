/** ~ CopyRight © 2012 China Mobile Group Anhui CO.,LTD All Rights Reserved. */
package com.cmcc.zysoft.groupaddressbook.util;

/**
 * @description 我的历史订单：分页
 * @author zhangfengtian
 * @email zhang.fengtian@ustcinfo.com
 * @date 2012-12-16 下午12:23:52
 */
public class PaginationUtil {
	private int page; // 当前页数
	private int rows; // 每页显示个数
	private int totalNum; // 总的记录数
	private int totalPage; // 总的页数
	private int frontNum = 5; // 前面显示的链接数,页面上需要显示的链接个数应为（frontNum+backNum）
	private int backNum = 2; // 后面显示的链接数
	private int beginPage; // 第一部分开始显示的链接的下标值
	private int endPage; // 第一部分最后显示的链接的下标值
	private int afterPage; // 第二部分开始显示的链接的下标值
	private int isShowAll;// 是否显示所有的链接

	/**
	 * @param page 当前页数
	 * @param totalPage 总的页数
	 */
	public PaginationUtil(int page, int totalPage) {
		super();
		this.page = page;
		this.totalPage = totalPage;
		setBeginEndPage();
	}

	/**
	 * @param page 当前页数
	 * @param totalPage 总的页数
	 * @param frontNum 前面显示的链接数,页面上需要显示的链接个数应为（frontNum+backNum）
	 * @param backNum 后面显示的链接数
	 */
	public PaginationUtil(int page, int totalPage, int frontNum, int backNum) {
		super();
		this.page = page;
		this.totalPage = totalPage;
		this.frontNum = frontNum;
		this.backNum = backNum;
		setBeginEndPage();
	}

	/**
	 * @param page 当前页数
	 * @param rows 每页显示个数
	 * @param totalNum 总的记录数
	 * @param totalPage 总的页数
	 * @param frontNum 前面显示的链接数,页面上需要显示的链接个数应为（frontNum+backNum）
	 * @param backNum 后面显示的链接数
	 */
	public PaginationUtil(int page, int rows, int totalNum, int totalPage,
			int frontNum, int backNum) {
		super();
		this.page = page;
		this.rows = rows;
		this.totalNum = totalNum;
		this.totalPage = totalPage;
		this.frontNum = frontNum;
		this.backNum = backNum;
		setBeginEndPage();
	}

	/**
	 * 计算起始，结束显示的页数 下标的位置：1——>begin——>end——>after——>total.
	 * 判断的区间：[1,begin),[begin,end],(end,after),[after,total]
	 */
	private void setBeginEndPage() {
		if (frontNum + backNum >= totalPage) { // 显示所有链接
			beginPage = 1;
			endPage = totalPage;
			isShowAll = 1;
		} else { // 显示部分链接
			// 从page开始，不够显示需要的个数（frontNum+backNum）
			if (page + frontNum + backNum - 1 > totalPage) { 
				beginPage = totalPage - backNum - frontNum + 1;
				endPage = beginPage + frontNum - 1;
			} else { // 从page开始，够显示需要的个数
				beginPage = page;
				endPage = page + frontNum - 1;
			}
			afterPage = totalPage - backNum + 1;
			isShowAll = 0;
		}
	}

	/**
	 * @return int
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page 
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return int
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows 每页个数
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return int
	 */
	public int getTotalNum() {
		return totalNum;
	}

	/**
	 * @param totalNum 总个数
	 */
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	/**
	 * @return int
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @param totalPage 总页数
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * @return int
	 */
	public int getFrontNum() {
		return frontNum;
	}

	/**
	 * @param frontNum 前面个数
	 */
	public void setFrontNum(int frontNum) {
		this.frontNum = frontNum;
	}

	/**
	 * @return int
	 */
	public int getBackNum() {
		return backNum;
	}

	/**
	 * @param backNum 后面显示个数
	 */
	public void setBackNum(int backNum) {
		this.backNum = backNum;
	}

	/**
	 * @return int
	 */
	public int getBeginPage() {
		return beginPage;
	}

	/**
	 * @param beginPage 开始页
	 */
	public void setBeginPage(int beginPage) {
		this.beginPage = beginPage;
	}

	/**
	 * @return int
	 */
	public int getEndPage() {
		return endPage;
	}

	/**
	 * @param endPage 结束页
	 */
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	/**
	 * @return int
	 */
	public int getAfterPage() {
		return afterPage;
	}

	/**
	 * @param afterPage 后面的页
	 */
	public void setAfterPage(int afterPage) {
		this.afterPage = afterPage;
	}

	/**
	 * @return int
	 */
	public int getIsShowAll() {
		return isShowAll;
	}

	/**
	 * @param isShowAll 是否显示所有
	 */
	public void setIsShowAll(int isShowAll) {
		this.isShowAll = isShowAll;
	}
}
