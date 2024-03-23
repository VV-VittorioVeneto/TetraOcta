package com.vv.vvaddon.Utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PEMap {

    public static class LeafNode {
        public Entity entity;
        public LeafNode next;
        public int count;
        public int level;
        public ItemStack item;

        public LeafNode(Entity e, int c, ItemStack i, int l) {
            entity = e;
            next = null;
            count = c;
            level = l;
            item = i;
        }
    }

    public static class TreeNode {
        public Player player;
        private TreeNode next;
        public LeafNode pair;

        public TreeNode(Player p) {
            player = p;
            next = null;
            pair = null;
        }
    }

    TreeNode head;

    public boolean hasKey(Player p) {
        TreeNode temp = head;
        while (temp != null && temp.player != p) {
            temp = temp.next;
        }
        return temp != null && temp.player == p;
    }

    public TreeNode getKey(Player p) {
        TreeNode temp = head;
        while (temp != null && temp.player != p) {
            temp = temp.next;
        }
        return temp;
    }

    private void push(Player p, Entity e, int count, ItemStack i, int l) {
        TreeNode tnode;
        if (hasKey(p)) {
            tnode = getKey(p);
        } else {
            tnode = new TreeNode(p);
            tnode.next = head;
            head = tnode;
        }
        LeafNode lnode = new LeafNode(e, count, i, l);
        lnode.next = tnode.pair;
        tnode.pair = lnode;
    }

    public void update(Player p, Entity e, int count, ItemStack i, int l) {
        TreeNode temp = getKey(p);
        if (temp == null) {
            push(p, e, count, i, l);
        } else {
            LeafNode templ = temp.pair;
            while (templ != null && templ.entity != e) {
                templ = templ.next;
            }
            if (templ == null) {
                push(p, e, count, i, l);
            } else {
                templ.count = count;
                templ.item = i;
                templ.level = l;
            }
        }
    }

    private void dropT(Player p) {
        TreeNode temp = head;
        TreeNode prev = null;
        if (temp != null && temp.player == p) {
            head = temp.next;
        }
        while (temp != null && temp.player != p) {
            prev = temp;
            temp = temp.next;
        }
        if (temp == null) {
            return;
        } else if (prev != null) {
            prev.next = temp.next;
        }
    }

    public void drop(Player p, Entity e) {
        TreeNode temp = getKey(p);
        if (temp == null) {
            return;
        } else {
            LeafNode templ = temp.pair;
            LeafNode prev = null;
            if (templ != null && templ.entity == e) {
                temp.pair = templ.next;
                if (temp.pair == null) dropT(p);
                return;
            }
            while (templ != null && templ.entity != e) {
                prev = templ;
                templ = templ.next;
            }
            if (templ != null && prev != null) {
                prev.next = templ.next;
            }
            if (temp.pair == null) dropT(p);
            return;
        }
    }

    public void drop(Entity e) {
        TreeNode temp = head;
        while (temp != null) {
            if (temp.pair != null && temp.pair.entity == e) {
                temp.pair = temp.pair.next;
            }
            if (temp.pair == null) {
                dropT(temp.player);
                return;
            } else {
                LeafNode prev = temp.pair;
                LeafNode templ = temp.pair.next;
                while (templ != null) {
                    if (templ.entity == e) {
                        prev.next = templ.next;
                        if (temp.pair == null) {
                            dropT(temp.player);
                            return;
                        }
                    }
                    prev = templ;
                    templ = templ.next;
                }
                temp = temp.next;
            }
        }
    }

    public int getsize() {
        int i = 0;
        TreeNode temp = head;
        while (temp != null) {
            LeafNode templ = temp.pair;
            while (templ != null) {
                templ = templ.next;
                i++;
            }
            temp = temp.next;
        }
        return i;
    }
}
