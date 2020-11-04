package subway.control;

import java.util.ArrayList;
import java.util.List;

import subway.model.Beanstation;;

public class lineprocess<G> {
/*
 * 建图
 */
	private static final int MAX= 999;
	private int[][] matirx;//地铁线路图的邻接矩阵
	public List<G> vertex;//顶点
	public int[][] getMatirx() {
		return matirx;
	}

	//初始化邻接矩阵
	public lineprocess(List<G> vertices) {
		this.vertex = vertices;
		int size = this.vertex.size();
		this.matirx = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(i==j) this.matirx[i][j]=0;
				else  this.matirx[i][j]=MAX;
			}
		}
	}
	public void edg(G start, G stop, int a) {//站点间的边边
		int i = vertex.indexOf(start);
		int j = vertex.indexOf(stop);
		int n = matirx.length;
		if (i >= 0 && i < n && j >= 0 && j < n&& this.matirx[i][j] == MAX && i != j) {
			this.matirx[i][j] = a;
			this.matirx[j][i] = a;
		}
	}
 

/*
 * floyd算法
 */
	private int[][] D = null;//顶点间的最小路径值矩阵
	private int[][] P = null;//对应点的最小路径的前驱点，例如p(1,3) = 2 说明顶点1到顶点3的最小路径要经过2 
	private int[][][] path = null;
	private int[] QAQ =null;
	public void floyd(int[][] Graph) {
		int vexnum = Graph.length;//顶点数
		this.D = Graph;//初始化D矩阵
		this.P = new int[vexnum][vexnum];
		this.QAQ = new int[vexnum];
		this.path = new int[vexnum][vexnum][];
		//初始化P矩阵 
		for (int v = 0; v < vexnum; v++) {
			QAQ[v] = -1;
			for (int w = 0; w < vexnum; w++)
			P[v][w] =-1;
		}		
		//这里是弗洛伊德算法的核心部分
		 //k为中间点 
		for (int k = 0; k < vexnum; k++) {
			//v为起点 
			for (int v = 0; v < vexnum; v++) {
				//w为终点
				for (int w = 0; w < vexnum; w++) {
					if (D[v][w] > D[v][k] + D[k][w]) {
						D[v][w] = D[v][k] + D[k][w];//更新最小路径
						P[v][w] = k;//更新最小路径中间顶点 
					}
				}
			}
		}
		//v->w的路径
		for (int v = 0; v < vexnum; v++) {
			int[] lenth = new int[1];//经过的点数
			for (int w = 0; w < vexnum; w++) {
				lenth[0] = 0;
				//起点为v
				QAQ[lenth[0]++] = v;
				resultpath(P, v, w, QAQ, lenth);// 更新QAQ
				path[v][w] = new int[lenth[0]];
				for (int s = 0; s < lenth[0]; s++)
					path[v][w][s] = QAQ[s];
			}
		}
	}
	// 输出v到w的路径
	private void resultpath(int[][] P, int v, int w, int[] QAQ, int[] lenth) {
		if (v == w) return ;
		if (P[v][w] == -1)
			QAQ[lenth[0]++] = w;
		else {
			resultpath(P, v, P[v][w], QAQ, lenth);
			resultpath(P, P[v][w], w, QAQ, lenth);
		}
	}
	public int[] getpath(int startPos, int stopPos) {
		return path[startPos][stopPos];
	}
	
/*
 * 查询输出部分
 */
	public StringBuffer output(G start, G stop,int[][] sub,List<Beanstation> lines) {
		StringBuffer result = new StringBuffer();
		floyd(sub);
		int from = vertex.indexOf(start);
		int to = vertex.indexOf(stop);
		int[] path = getpath(from, to);
		result.append(start + "->" + stop + "的最短路线为:\n");
		int k=0;
		int count=0;
		for (int i : path) {
			k++;
			result.append(vertex.get(i) + " -> ");
			if(k%23==0) {
				result.append(System.lineSeparator());
			}
		}
		result.delete(result.lastIndexOf(" -> "), result.length());
		List<String> cg=new ArrayList<>();//换乘站集合
		for(int j=1;j<(path.length)-1;j++) {
			int i=path[j];
			String s1=same(vertex.get(path[j-1])+"", vertex.get(i)+"", lines);
			String s2=same(vertex.get(i)+"", vertex.get(path[j+1])+"", lines);
			if(!s1.equals(s2)) {
				cg.add(vertex.get(i)+"");
			}
		}
		result.append(System.lineSeparator()+"换乘站点:"+System.lineSeparator());
		for(String s:cg) {
			result.append(s+System.lineSeparator());
			count++;
		}
		result.append("换乘站数:"+count+"\n");
		result.append("最短经过站点数： " + path.length);
		return result;
}
 
	public String same(String s1,String s2,List<Beanstation> lines) {
		String sb="";
		List<String> sbs1=new ArrayList<>();
		List<String> sbs2=new ArrayList<>();
		for(Beanstation r:lines) {
			for(String s:r.getStations()) {
				
				if(s.equals(s1)) {
					sbs1.add(r.getStationname());
				}
				if(s.equals(s2)) {
					sbs2.add(r.getStationname());
				}
			}
			
		}

		for(int i=0;i<sbs1.size();i++) {
			for(int j=0;j<sbs2.size();j++) {
				if(sbs1.get(i).equals(sbs2.get(j))) {
					sb=sbs1.get(i);
				}
			}
		}
		return sb;
	}
}
