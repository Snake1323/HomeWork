package semester1.practice11.homework;



public class Task2ArrayListCapacityAndDynamicGrowth {
    static class CustomArrayCapacityEmulator<E> {
        private static final int DEFAULT_CAPACITY = 10;
        private Object[] elementData;
        private int size = 0;
        private int resizeCount = 0;
        private int totalElementsCopied = 0;

        public CustomArrayCapacityEmulator() {
            elementData = new Object[DEFAULT_CAPACITY];
        }

        private void ensureCapacityInternal() {
            if (size == elementData.length) {
                int oldCapacity = elementData.length;
                int newCapacity = oldCapacity + (oldCapacity >> 1);
                // Після trimToSize() ємність може бути 0 або 1.
                if (newCapacity <= oldCapacity) newCapacity = oldCapacity + 1;
                Object[] newBuffer = new Object[newCapacity];
                System.arraycopy(elementData, 0, newBuffer, 0, size);
                elementData = newBuffer;
                resizeCount++;
                totalElementsCopied += size;
                System.out.println("Ємність: " + oldCapacity + " -> " + newCapacity);
            }
        }

        public void add(E element) {
            ensureCapacityInternal();
            elementData[size++] = element;
        }

        public void add(int index, E element) {
            if (index < 0 || index > size) throw new IndexOutOfBoundsException(index);
            ensureCapacityInternal();
            System.arraycopy(elementData, index, elementData, index + 1, size - index);
            elementData[index] = element;
            size++;
        }

        public E remove(int index) {
            E old = get(index);
            int numMoved = size - index - 1;
            if (numMoved > 0) {
                System.arraycopy(elementData, index + 1, elementData, index, numMoved);
            }
            elementData[--size] = null;
            return old;
        }

        @SuppressWarnings("unchecked")
        public E get(int index) {
            if (index < 0 || index >= size) throw new IndexOutOfBoundsException(index);
            return (E) elementData[index];
        }

        public E set(int index, E element) {
            E old = get(index);
            elementData[index] = element;
            return old;
        }

        public int size() {
            return size;
        }

        public int getCapacity() {
            return elementData.length;
        }

        public void trimToSize() {
            Object[] newBuffer = new Object[size];
            System.arraycopy(elementData, 0, newBuffer, 0, size);
            elementData = newBuffer;
        }
    }

    public static void main(String[] args) {
        var emulator = new CustomArrayCapacityEmulator<String>();
        // var raw = new ArrayList<>(); дає ArrayList<Object>, а не ArrayList<String>.
        System.out.println("size = " + emulator.size() + ", capacity = " + emulator.getCapacity());
        for (int i = 1; i <= 35; i++) {
            emulator.add("Block-" + i);
        }
        emulator.add(0, "HEADER");
        System.out.println("Видалено: " + emulator.remove(5));
        System.out.println("Перший: " + emulator.get(0) + ", останній: " + emulator.get(emulator.size() - 1));
        System.out.println("size = " + emulator.size() + ", capacity = " + emulator.getCapacity());
        System.out.println("Розширень: " + emulator.resizeCount + ", скопійовано при розширеннях: " + emulator.totalElementsCopied);
        emulator.trimToSize();
        System.out.println("Після trimToSize: size = " + emulator.size() + ", capacity = " + emulator.getCapacity());
    }
}
