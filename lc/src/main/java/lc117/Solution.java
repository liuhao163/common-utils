package lc117;

/**
 * @Author: liuhaoeric
 * Create time: 2019/12/23
 * Description:
 */
public class Solution {

    public static void main(String[] args) {

//        Node node1=new Node(1);
//        Node node2=new Node(2);
//         node3=new Node(3);
        Node node8 = new Node(8);
        Node node5 = new Node(5);
        Node node7 = new Node(7);
        Node node6 = new Node(6,null, node8, null);
        Node node3 = new Node(3, null, node6, null);
        Node node4 = new Node(4,node7,null,null);
        Node node2 = new Node(2, node4, node5, null);
        Node node1 = new Node(1, node2, node3, null);


        Node ret=new Solution().connect(node1);

    }

    public Node connect(Node root) {
        if (root == null) {
            return null;
        }
        Node left = root.left;
        Node right = root.right;
        fillNext(left, right, null);
        fillNext(right, null, null);
        return root;
    }

    public void fillNext(Node node, Node right, Node uncle) {
        if (node == null) {
            return;
        }

        if (right != null) {
            node.next = right;
        } else if (uncle != null && uncle.left != null) {
            node.next = uncle.left;
        } else if (uncle != null && uncle.right != null) {
            node.next = uncle.right;
        }

        fillNext(node.left, node.right, node.next);
        fillNext(node.right, null, node.next);
    }
}


//    func FillNext(this *Node, sibling *Node) {
//        if this == nil {
//            return
//        }
//
//        this.Left.Next = this.Right
//        if this.Right != nil && sibling != nil{
//            if sibling.Left!=nil{
//                this.Right.Next = sibling.Left
//            }else {
//                this.Right.Next = sibling.Right
//            }
//        }
//        FillNext(this.Left, this.Left.Next)
//        FillNext(this.Right, this.Right.Next)
//    }
