package org.zavaglia.advent2021;

public class SnailNumber {
    SnailNumber parent;
    SnailNumber left;
    SnailNumber right;
    int number;

    SnailNumber(String s, int pos) {
        parent = null;
        parseNumber(s, pos);
    }

    SnailNumber(SnailNumber parent) {
        this.parent = parent;
    }

    static SnailNumber add(SnailNumber lhs, SnailNumber rhs) {
        var sn = new SnailNumber(null);
        sn.left = lhs;
        lhs.parent = sn;
        sn.right = rhs;
        rhs.parent = sn;
        sn.reduce();
        return sn;
    }

    boolean isPair() {
        return left != null;
    }

    int parseNumber(String line, int pos) {
        if (line.charAt(pos) == '[') {
            this.left = new SnailNumber(this);
            this.right = new SnailNumber(this);
            pos = this.left.parseNumber(line, pos + 1);
            assert line.charAt(pos) == ',';
            pos = this.right.parseNumber(line, pos + 1);
            assert line.charAt(pos) == ']';
            return pos + 1;
        } else {
            this.number = line.charAt(pos) - '0';
            return pos + 1;
        }
    }

    int depth() {
        var sn = this;
        var count = -1; // will be incremented a minimum of 1 time
        while (sn != null) {
            sn = sn.parent;
            count++;
        }
        return count;
    }

    void addToLeftNumber(int n) {
        // find node which is not a left child
        var node = this;
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        if (node.parent == null) {
            // reached root and didn't find an in-order left node
            return;
        }
        assert node.parent.right == node;
        node = node.parent.left;
        while (node.isPair()) {
            node = node.right;
        }
        node.number += n;
    }

    void addToRightNumber(int n) {
        // find node which is not a right child
        var node = this;
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        if (node.parent == null) {
            // reached root and didn't find an in-order left node
            return;
        }
        assert node.parent.left == node;
        node = node.parent.right;
        while (node.isPair()) {
            node = node.left;
        }
        node.number += n;
    }

    @Override
    public String toString() {
        if (!isPair()) {
            return Integer.toString(number);
        }
        return "[" + left.toString() + "," + right.toString() + "]";
    }

    boolean explode() {
        if (depth() == 4 && isPair()) {
            assert !left.isPair();
            assert !right.isPair();
            addToLeftNumber(left.number);
            addToRightNumber(right.number);
            left = null;
            right = null;
            number = 0;
            return true;
        }

        if (left != null && left.explode()) {
            return true;
        }
        return right != null && right.explode();
    }

    boolean split() {
        if (!isPair()) {
            if (number > 9) {
                left = new SnailNumber(this);
                right = new SnailNumber(this);
                left.number = number / 2;
                right.number = number / 2 + number % 2;
                number = 0;
                return true;
            } else {
                return false;
            }
        } else {
            return left.split() || right.split();
        }
    }

    void reduce() {
        while (true) {
            if (!explode())
                if (!split())
                    break;
        }
    }

    long magnitude() {
        if (!isPair())
            return number;
        return 3 * left.magnitude() + 2 * right.magnitude();
    }

}
