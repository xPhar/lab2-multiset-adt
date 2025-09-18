import java.util.ArrayList;
import java.util.Random;

public class Tree {
    // We recommend attempting this class last, as it hasn't been scaffolded for your team.
    // Even if your team doesn't have time to implement this class, it is a useful exercise
    // to think about how you might split up the work to get the Tree and TreeMultiSet
    // implemented.
    Integer root;
    private ArrayList<Tree> subtrees;

    // Precondition: input will not be used
    public Tree(Integer root, ArrayList<Tree> subtree){
        this.root = root;
        subtrees = new ArrayList<>(subtree);

        /* Although the python code write like this, but I need to tell that
         * here will be reference copy problem:
         * Even when I create a new ArrayList, the reference in ArrayList points
         * to the tree instance is the same.
         * If the outside code modify a tree in the ArrayList, Then this tree
         * will be modified as well.
         * So I will add a precondition that the input trees won't be used in
         * outside this .java file.
         * If we want to solve it completely, we need to write deepcopy.
         * That is, copying every layers' trees, one element by one element.
         * Forturnately, we do not need to handle this issue,
         * because our TreeMultiSet only maintain 1 tree form empty.
         */
    }

    public Tree(Integer root) {
        this(root, new ArrayList<>());
    }

    public Tree() {
        this(null);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int getSize() {
        if (this.isEmpty()) return 0;
        int size = 1;
        for (Tree subtree : subtrees) size += subtree.getSize();
        return size;
    }

    public int count(int item) {
        if (this.isEmpty()) return 0;
        int count = 0;
        if (item == this.root) count++;
        for (Tree subtree : subtrees) count += subtree.count(item);
        return count;
    }

    @Override
    public String toString() {
        return this.strIndented();
    }

    private String strIndented(int depth) {
        if (this.isEmpty()) return "";
        StringBuilder s = new StringBuilder();
        s.append("  ".repeat(depth));
        s.append(this.root).append("\n");
        for (Tree subtree : this.subtrees) s.append(subtree.strIndented(depth + 1));
        return s.toString();
    }

    private String strIndented() {
        return strIndented(0);
    }

    public double average() {
        if (this.isEmpty()) return 0.0;
        int[] tmp = averageHelper();
        int total = tmp[0], count = tmp[1];
        return (double) total / (double) count;

    }

    private int[] averageHelper() {
        if (this.isEmpty()) return new int[]{0, 0};
        int total = root;
        int count = 1;
        for (Tree subtree : this.subtrees) {
            int[] tmp = subtree.averageHelper();
            total += tmp[0];
            count += tmp[1];
        }return new int[]{total, count};
    }

    public boolean equals(Tree other) {
        if (this.isEmpty() && other.isEmpty()) return true;
        if (this.isEmpty() || other.isEmpty()) return false;
        if (!this.root.equals(other.root)) return false;
        if (this.subtrees.size() != other.subtrees.size()) return false;
        return this.subtrees.equals(other.subtrees);
    }

    public boolean contains(int item) {
        if (this.isEmpty()) return false;
        if (item == this.root) return true;
        for (Tree subtree : this.subtrees) if (subtree.contains(item)) return true;
        return false;
    }

    public ArrayList<Integer> leaves() {
        ArrayList<Integer> leaves = new ArrayList<>();
        if (this.isEmpty()) return leaves;
        if (this.subtrees.isEmpty()) {
            leaves.add(this.root);
            return leaves;
        }
        for (Tree subtree : this.subtrees) {
            leaves.addAll(subtree.leaves());
        }return leaves;
    }

    public boolean deleteItem(int item) {
        if (this.isEmpty()) return false;
        if (this.root.equals(item)) {
            deleteRoot();
            return true;
        }
        for (Tree subtree : this.subtrees) {
            boolean deleted = subtree.deleteItem(item);
            if (deleted && subtree.isEmpty()) {
                subtrees.remove(subtree);
                return true;
            }
            if (deleted) return true;
        }
        return false;
    }

    private void deleteRoot() {
        if (subtrees.isEmpty()) root = null;
        else {
            Tree chosenSubtree = subtrees.remove(subtrees.size()-1);
            root = chosenSubtree.root;
            subtrees.addAll(chosenSubtree.subtrees);
        }

    }

    private int extractLeaf() {
        if (subtrees.isEmpty()) {
            int oldRoot = root;
            root = null;
            return oldRoot;
        }
        int leaf = subtrees.get(0).extractLeaf();
        if (subtrees.get(0).isEmpty()) subtrees.remove(0);
        return leaf;
    }

    public void insert(int item) {
        if (this.isEmpty()) root = item;
        else if (subtrees.isEmpty()){
            subtrees = new ArrayList<>();
            subtrees.add(new Tree(item));
        }else {
            Random random = new Random();
            if (random.nextInt(3) + 1 == 3) {
                subtrees.add(new Tree(item));
            }else {
                int subtreeIndex = random.nextInt(subtrees.size());
                subtrees.get(subtreeIndex).insert(item);
            }
        }
    }

    public boolean insertChild(int item, int parent) {
        if (this.isEmpty()) return false;
        if (root.equals(parent)){
            subtrees.add(new Tree(item));
            return true;
        }
        for (Tree subtree : subtrees) if (subtree.insertChild(item, parent)) return true;
        return false;
    }
}

