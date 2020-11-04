package subway.control;

import java.util.ArrayList;
import java.util.List;

import subway.model.Beanstation;;

public class lineprocess<G> {
/*
 * ��ͼ
 */
	private static final int MAX= 999;
	private int[][] matirx;//������·ͼ���ڽӾ���
	public List<G> vertex;//����
	public int[][] getMatirx() {
		return matirx;
	}

	//��ʼ���ڽӾ���
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
	public void edg(G start, G stop, int a) {//վ���ı߱�
		int i = vertex.indexOf(start);
		int j = vertex.indexOf(stop);
		int n = matirx.length;
		if (i >= 0 && i < n && j >= 0 && j < n&& this.matirx[i][j] == MAX && i != j) {
			this.matirx[i][j] = a;
			this.matirx[j][i] = a;
		}
	}
 

/*
 * floyd�㷨
 */
	private int[][] D = null;//��������С·��ֵ����
	private int[][] P = null;//��Ӧ�����С·����ǰ���㣬����p(1,3) = 2 ˵������1������3����С·��Ҫ����2 
	private int[][][] path = null;
	private int[] QAQ =null;
	public void floyd(int[][] Graph) {
		int vexnum = Graph.length;//������
		this.D = Graph;//��ʼ��D����
		this.P = new int[vexnum][vexnum];
		this.QAQ = new int[vexnum];
		this.path = new int[vexnum][vexnum][];
		//��ʼ��P���� 
		for (int v = 0; v < vexnum; v++) {
			QAQ[v] = -1;
			for (int w = 0; w < vexnum; w++)
			P[v][w] =-1;
		}		
		//�����Ǹ��������㷨�ĺ��Ĳ���
		 //kΪ�м�� 
		for (int k = 0; k < vexnum; k++) {
			//vΪ��� 
			for (int v = 0; v < vexnum; v++) {
				//wΪ�յ�
				for (int w = 0; w < vexnum; w++) {
					if (D[v][w] > D[v][k] + D[k][w]) {
						D[v][w] = D[v][k] + D[k][w];//������С·��
						P[v][w] = k;//������С·���м䶥�� 
					}
				}
			}
		}
		//v->w��·��
		for (int v = 0; v < vexnum; v++) {
			int[] lenth = new int[1];//�����ĵ���
			for (int w = 0; w < vexnum; w++) {
				lenth[0] = 0;
				//���Ϊv
				QAQ[lenth[0]++] = v;
				resultpath(P, v, w, QAQ, lenth);// ����QAQ
				path[v][w] = new int[lenth[0]];
				for (int s = 0; s < lenth[0]; s++)
					path[v][w][s] = QAQ[s];
			}
		}
	}
	// ���v��w��·��
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
 * ��ѯ�������
 */
	public StringBuffer output(G start, G stop,int[][] sub,List<Beanstation> lines) {
		StringBuffer result = new StringBuffer();
		floyd(sub);
		int from = vertex.indexOf(start);
		int to = vertex.indexOf(stop);
		int[] path = getpath(from, to);
		result.append(start + "->" + stop + "�����·��Ϊ:\n");
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
		List<String> cg=new ArrayList<>();//����վ����
		for(int j=1;j<(path.length)-1;j++) {
			int i=path[j];
			String s1=same(vertex.get(path[j-1])+"", vertex.get(i)+"", lines);
			String s2=same(vertex.get(i)+"", vertex.get(path[j+1])+"", lines);
			if(!s1.equals(s2)) {
				cg.add(vertex.get(i)+"");
			}
		}
		result.append(System.lineSeparator()+"����վ��:"+System.lineSeparator());
		for(String s:cg) {
			result.append(s+System.lineSeparator());
			count++;
		}
		result.append("����վ��:"+count+"\n");
		result.append("��̾���վ������ " + path.length);
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
