package tw.kewang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysInfoHolder {
    private static final Logger LOG = LoggerFactory.getLogger(SysInfoHolder.class);
    private static final ThreadLocal<SysInfo> SYS_INFO_HOLDER = new ThreadLocal<>();

    /**
     * Set the SysInfo to Holder for followed usage.
     * @param sysInfo
     */
    public static void setSysInfo(SysInfo sysInfo) {
        SYS_INFO_HOLDER.set(sysInfo);
    }

    /**
     * Get the SysInfo from Holder.
     * @return
     */
    public static SysInfo getSysInfo() {
        return SYS_INFO_HOLDER.get();
    }

    /**
     * Clear the Holder.
     */
    public static void clear() {
        SYS_INFO_HOLDER.set(null);
    }
}