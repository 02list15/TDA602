package backEnd;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Wallet {
    /**
     * The RandomAccessFile of the wallet file
     */
    private RandomAccessFile file;

    /**
     * Creates a Wallet object
     *
     * A Wallet object interfaces with the wallet RandomAccessFile
     */
    public Wallet() throws Exception {
        this.file = new RandomAccessFile(new File("backEnd/wallet.txt"), "rw");
    }

    /**
     * Gets the wallet balance.
     *
     * @return The content of the wallet file as an integer
     */
    public int getBalance() throws IOException {
        this.file.seek(0);
        return Integer.parseInt(this.file.readLine());
    }

    /**
     * Sets a new balance in the wallet
     *
     * @param newBalance new balance to write in the wallet
     */
    public void setBalance(int newBalance) throws Exception {
        this.file.setLength(0);
        String str = Integer.valueOf(newBalance).toString() + '\n';
        this.file.writeBytes(str);
    }

    /**
     * Closes the RandomAccessFile in this.file
     */
    public void close() throws Exception {
	    this.file.close();
    }

    public boolean safeWithdraw(int valueToWithdraw) throws Exception {
        FileChannel chan = this.file.getChannel();
        try (FileLock lock = chan.lock()) {
            int balance = getBalance();
            if (balance < valueToWithdraw) {
                return false;
            }
            setBalance(balance - valueToWithdraw);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
