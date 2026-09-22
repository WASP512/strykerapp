package com.zalexdev.stryker.guide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.zalexdev.stryker.BuildConfig;
import com.zalexdev.stryker.R;

/**
 * In-app tutorial / user guide.
 *
 * Purely presentational: it explains prerequisites, first-launch setup, what every
 * module is for and where to find it, and the responsible-use expectations. All content
 * is rendered programmatically so the section list can grow without hand-editing a huge
 * layout, and so it inherits the app's Material 3 card language and light/dark colors.
 */
public class GuideFragment extends Fragment {

    private static final String GITHUB_URL = "https://github.com/zalexdev/strykerapp";

    private float density;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        density = getResources().getDisplayMetrics().density;

        LinearLayout root = view.findViewById(R.id.guide_container);
        if (root == null) return;

        buildHero(root);
        buildAuthorizedUse(root);

        sectionHeader(root, "GETTING STARTED", 0xFF1565C0);
        card(root, R.drawable.info_outlined, 0xFF1565C0,
                "What StrykerOSS is",
                "StrykerOSS is a unified front-end for a curated set of open-source network, "
                        + "wireless and web security tools. Heavyweight tools (Nmap, Metasploit, "
                        + "Nuclei, Hydra, SearchSploit, …) run natively inside a Debian arm64 "
                        + "environment that Stryker manages for you — you drive them from a modern "
                        + "UI instead of memorising command lines.\n\n"
                        + "Everything is grouped in the left-hand navigation drawer. Open it with "
                        + "the menu button in the top bar, then tap a module to switch to it.");
        card(root, R.drawable.ic_outline_help, 0xFF1565C0,
                "Prerequisites",
                "• A rooted Android device (Magisk or KernelSU) is recommended for the full "
                        + "feature set. If root is unavailable, Stryker can fall back to a rootless "
                        + "QEMU VM for the tools that don't need the phone's own hardware.\n\n"
                        + "• About 1 GB of free internal storage for the environment, tools and "
                        + "signatures.\n\n"
                        + "• An external monitor-mode USB Wi-Fi adapter (e.g. Atheros AR9271 or a "
                        + "Realtek 88XXAU) for the Wi-Fi capture features — the phone's built-in "
                        + "radio can't do these.\n\n"
                        + "• A USB-gadget-capable kernel for HID Attacks and USB Arsenal "
                        + "(CONFIG_USB_CONFIGFS and a populated /sys/class/udc/). Most NetHunter and "
                        + "modern OEM kernels already qualify.");
        card(root, R.drawable.rocket_launch, 0xFF00897B,
                "Choosing an engine: root vs rootless VM",
                "On first launch Stryker asks how it should run the tool environment:\n\n"
                        + "• Root (chroot) — tools run directly on the device through a Debian "
                        + "chroot. Fastest, and the only mode that can reach the phone's own radios "
                        + "and USB-gadget hardware. Needs a working su.\n\n"
                        + "• Rootless (VM) — tools run inside a bundled QEMU virtual machine, no root "
                        + "required. Network, web and database tools work fine here; features that "
                        + "need the phone's physical radio or USB gadget (marked ROOT in the drawer) "
                        + "are unavailable and appear dimmed.\n\n"
                        + "You can revisit this from the Dashboard / Core manager.");
        card(root, R.drawable.rocket_launch, 0xFF00897B,
                "First-launch setup",
                "The built-in installer walks you through it step by step:\n\n"
                        + "1. Grant root (su) when prompted, or pick the rootless VM.\n"
                        + "2. Accept the runtime permissions Android asks for (storage, location, "
                        + "notifications, Bluetooth, audio) — modules are disabled until their "
                        + "permission is granted.\n"
                        + "3. Let it download and unpack the environment core. This is the biggest "
                        + "download; use Wi-Fi and don't kill the app.\n"
                        + "4. Optionally install extra components (Metasploit, Nuclei, Hydra, "
                        + "SearchSploit) — these can also be installed later from their own screens.\n\n"
                        + "When the Dashboard shows the environment as mounted/ready, you're set.");
        card(root, R.drawable.terminal, 0xFF1565C0,
                "The built-in terminal",
                "Drawer → Terminal (or the separate \"Stryker Terminal\" launcher icon) drops you "
                        + "straight into a shell inside the managed environment — no external terminal "
                        + "app needed. Use it to run tools by hand, check versions, or inspect output "
                        + "that the module UIs don't surface.");

        sectionHeader(root, "MODULES", 0xFF5E35B1);
        card(root, R.drawable.home, 0xFF1565C0,
                "Dashboard",
                "Your home screen: a live overview of the environment (mounted or not), detected "
                        + "USB adapters, the active engine, and quick actions. Start here to confirm "
                        + "everything is ready before running a module.");
        card(root, R.drawable.wifi, 0xFF1565C0,
                "WiFi networks",
                "Scan nearby networks and run the wireless workflows (deauthentication, handshake "
                        + "capture, WPS checks) through an external monitor-mode adapter. Needs root "
                        + "and a supported USB Wi-Fi adapter — plug it in first, and only ever test "
                        + "networks you are authorised to.");
        card(root, R.drawable.storage, 0xFF00897B,
                "Handshakes",
                "Local storage for captured handshakes: rename, share, export, or attempt on-device "
                        + "cracking with Hashcat against wordlists you provide. Captures from the WiFi "
                        + "module land here automatically.");
        card(root, R.drawable.password, 0xFF1565C0,
                "MAC changer",
                "Randomise or set your adapter's MAC address, with saveable profiles. Handy for "
                        + "returning an interface to a known address. Needs root.");
        card(root, R.drawable.wpair, 0xFF3949AB,
                "WhisperPair (BLE)",
                "Bluetooth LE Fast Pair discovery and research tooling, including a vulnerability "
                        + "check and audio capabilities. Works without root on the VM. For research "
                        + "against devices you own or are authorised to assess.");
        card(root, R.drawable.lan, 0xFFAB47BC,
                "Local network",
                "Discover hosts on the LAN, run port scans and OS fingerprinting, and dispatch "
                        + "per-device actions with a live terminal. A good first stop for mapping a "
                        + "network you're allowed to test.");
        card(root, R.drawable.scanner, 0xFFAB47BC,
                "Nmap",
                "A direct interface to Nmap: custom flags, NSE scripts and exportable reports, for "
                        + "when you want full control over a scan rather than the guided Local network "
                        + "view.");
        card(root, R.drawable.webscan, 0xFF3949AB,
                "Web scanner (Nuclei)",
                "Run Nuclei template scans against one or more web targets and browse findings "
                        + "grouped by severity, with the evidence for each. Install the Nuclei "
                        + "component first if you skipped it during setup.");
        card(root, R.drawable.motion_blur, 0xFFEF6C00,
                "Arsenal",
                "Your own database of exploit/scanner commands. Templates support placeholders like "
                        + "{IP}, {PORT}, {MAC}, {GW} and {MASK} that Stryker fills in from the current "
                        + "target, so you can re-run your favourite commands without retyping them.");
        card(root, R.drawable.keyboard, 0xFFC62828,
                "HID Attacks",
                "DuckyScript-compatible USB HID injection with bundled keyboard layouts, a live "
                        + "execution log and sample payloads. Needs root and a USB-gadget-capable "
                        + "kernel; the device acts as a keyboard toward whatever it's plugged into, so "
                        + "only use it on machines you own or are authorised to test.");
        card(root, R.drawable.shield, 0xFFC62828,
                "Metasploit",
                "A native Metasploit Framework console inside the environment — sessions, payload "
                        + "generation and a module browser. Install the Metasploit component first if "
                        + "you skipped it during setup.");
        card(root, R.drawable.map, 0xFF00838F,
                "GeoMac",
                "Plot captured BSSIDs and handshakes on an OpenStreetMap view and export in "
                        + "WiGLE-style KML/CSV. Needs location data to place points.");
        card(root, R.drawable.vnc, 0xFF5E35B1,
                "VNC desktop",
                "Bring up an XFCE desktop inside the environment and view it locally over VNC — "
                        + "useful for GUI tools that don't fit a terminal.");
        card(root, R.drawable.usb, 0xFF1565C0,
                "USB Arsenal",
                "Manage USB-gadget profiles: toggle HID keyboard/mouse, mass-storage and network "
                        + "(RNDIS/ECM/ACM) functions on the fly, customise VID/PID/serial and mount "
                        + "disk images. Needs root and a gadget-capable kernel.");
        card(root, R.drawable.tune, 0xFF5E35B1,
                "Core manager",
                "Mount, unmount or repair the environment and manage installed components. Come here "
                        + "if a module reports the environment isn't ready, or to reclaim space.");

        sectionHeader(root, "TROUBLESHOOTING", 0xFFEF6C00);
        card(root, R.drawable.ic_outline_help, 0xFFEF6C00,
                "A module is greyed out",
                "Dimmed rows in the drawer mean the current engine can't run that module. Rows marked "
                        + "ROOT need the phone's own radio or USB gadget and don't work on the rootless "
                        + "VM — switch to a rooted engine. Others need the VM to be started first: open "
                        + "the Dashboard and start it, then return.");
        card(root, R.drawable.ic_outline_help, 0xFFEF6C00,
                "Wi-Fi features don't work",
                "The phone's built-in Wi-Fi can't be used for capture. Plug in a supported external "
                        + "monitor-mode adapter (AR9271 / 88XXAU) before opening the WiFi module, and "
                        + "confirm it appears on the Dashboard.");
        card(root, R.drawable.ic_outline_help, 0xFFEF6C00,
                "Setup download failed or the environment won't mount",
                "Re-run the install from Core manager. Make sure you have ~1 GB free and a stable "
                        + "connection, and don't background the app mid-download. Core manager can also "
                        + "repair a partially-installed environment.");
        card(root, R.drawable.ic_outline_help, 0xFFEF6C00,
                "A tool is missing",
                "Metasploit, Nuclei, Hydra and SearchSploit are optional components. If you skipped "
                        + "them during first launch, install them from their own module screens or from "
                        + "Core manager.");

        buildFooter(root);
    }

    // ---- builders -------------------------------------------------------------------------

    private void buildHero(@NonNull LinearLayout root) {
        MaterialCardView cardView = new MaterialCardView(root.getContext());
        cardView.setRadius(dp(20));
        cardView.setCardElevation(0f);
        cardView.setCardBackgroundColor(0xFF1565C0);
        cardView.setLayoutParams(cardParams(0));

        LinearLayout col = new LinearLayout(root.getContext());
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(dp(20), dp(22), dp(20), dp(22));

        MaterialTextView title = new MaterialTextView(root.getContext());
        title.setText("Welcome to StrykerOSS");
        title.setTextColor(Color.WHITE);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        col.addView(title);

        MaterialTextView sub = new MaterialTextView(root.getContext());
        sub.setText("A mobile pentest suite for authorized testing. This guide covers what you "
                + "need, how to set it up, and what every module does.");
        sub.setTextColor(0xCCFFFFFF);
        sub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sp.topMargin = dp(8);
        sub.setLayoutParams(sp);
        col.addView(sub);

        MaterialTextView ver = new MaterialTextView(root.getContext());
        ver.setText("v" + BuildConfig.VERSION_NAME);
        ver.setTextColor(0x99FFFFFF);
        ver.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
        ver.setTypeface(Typeface.MONOSPACE);
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vp.topMargin = dp(12);
        ver.setLayoutParams(vp);
        col.addView(ver);

        cardView.addView(col);
        root.addView(cardView);
    }

    private void buildAuthorizedUse(@NonNull LinearLayout root) {
        MaterialCardView cardView = new MaterialCardView(root.getContext());
        cardView.setRadius(dp(16));
        cardView.setCardElevation(0f);
        cardView.setCardBackgroundColor(ContextCompat.getColor(root.getContext(), R.color.light_contrast));
        cardView.setStrokeColor(0xFFC62828);
        cardView.setStrokeWidth(dp(1));
        cardView.setLayoutParams(cardParams(dp(12)));

        LinearLayout rowCol = new LinearLayout(root.getContext());
        rowCol.setOrientation(LinearLayout.VERTICAL);
        rowCol.setPadding(dp(16), dp(16), dp(16), dp(16));

        MaterialTextView title = new MaterialTextView(root.getContext());
        title.setText("Authorized testing only");
        title.setTextColor(0xFFC62828);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        rowCol.addView(title);

        MaterialTextView body = new MaterialTextView(root.getContext());
        body.setText("StrykerOSS is for security testing, education and research on systems and "
                + "devices you own or have explicit, documented permission to test. Scanning, "
                + "capturing, injecting or attacking anything else may be illegal. You are "
                + "responsible for complying with all applicable laws and for obtaining permission "
                + "first. The authors accept no liability for misuse.");
        body.setTextColor(secondaryTextColor());
        body.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        body.setLineSpacing(dp(3), 1f);
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bp.topMargin = dp(8);
        body.setLayoutParams(bp);
        rowCol.addView(body);

        cardView.addView(rowCol);
        root.addView(cardView);
    }

    private void sectionHeader(@NonNull LinearLayout root, @NonNull String text, int color) {
        MaterialTextView label = new MaterialTextView(root.getContext());
        label.setText(text);
        label.setAllCaps(true);
        label.setLetterSpacing(0.08f);
        label.setTextColor(color);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
        label.setTypeface(label.getTypeface(), Typeface.BOLD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = dp(6);
        lp.topMargin = dp(24);
        lp.bottomMargin = dp(10);
        label.setLayoutParams(lp);
        root.addView(label);
    }

    /** A collapsible card: icon + title always visible, body toggles on tap. */
    private void card(@NonNull LinearLayout root, @DrawableRes int iconRes, int accent,
                      @NonNull String title, @NonNull String bodyText) {
        MaterialCardView cardView = new MaterialCardView(root.getContext());
        cardView.setRadius(dp(16));
        cardView.setCardElevation(0f);
        cardView.setCardBackgroundColor(ContextCompat.getColor(root.getContext(), R.color.light_contrast));
        cardView.setStrokeColor(ContextCompat.getColor(root.getContext(), R.color.light_lite_contrast));
        cardView.setStrokeWidth(dp(1));
        cardView.setClickable(true);
        cardView.setFocusable(true);
        cardView.setLayoutParams(cardParams(dp(10)));

        LinearLayout col = new LinearLayout(root.getContext());
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(dp(14), dp(14), dp(14), dp(14));

        // header row
        LinearLayout headerRow = new LinearLayout(root.getContext());
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);

        ImageView icon = new ImageView(root.getContext());
        icon.setImageResource(iconRes);
        icon.setColorFilter(accent);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(dp(22), dp(22));
        ip.rightMargin = dp(12);
        icon.setLayoutParams(ip);
        headerRow.addView(icon);

        MaterialTextView titleView = new MaterialTextView(root.getContext());
        titleView.setText(title);
        titleView.setTextColor(primaryTextColor());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        titleView.setTypeface(titleView.getTypeface(), Typeface.BOLD);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        titleView.setLayoutParams(tp);
        headerRow.addView(titleView);

        ImageView chevron = new ImageView(root.getContext());
        chevron.setImageResource(R.drawable.arrow_down);
        chevron.setColorFilter(ContextCompat.getColor(root.getContext(), R.color.grey));
        chevron.setRotation(0f);
        chevron.setLayoutParams(new LinearLayout.LayoutParams(dp(20), dp(20)));
        headerRow.addView(chevron);

        col.addView(headerRow);

        MaterialTextView body = new MaterialTextView(root.getContext());
        body.setText(bodyText);
        body.setTextColor(secondaryTextColor());
        body.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        body.setLineSpacing(dp(3), 1f);
        body.setVisibility(View.GONE);
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bp.topMargin = dp(10);
        body.setLayoutParams(bp);
        col.addView(body);

        cardView.addView(col);
        cardView.setOnClickListener(v -> {
            boolean show = body.getVisibility() != View.VISIBLE;
            body.setVisibility(show ? View.VISIBLE : View.GONE);
            chevron.animate().rotation(show ? 180f : 0f).setDuration(180L).start();
        });

        root.addView(cardView);
    }

    private void buildFooter(@NonNull LinearLayout root) {
        MaterialTextView link = new MaterialTextView(root.getContext());
        link.setText("Full documentation and source: github.com/zalexdev/strykerapp");
        link.setTextColor(ContextCompat.getColor(root.getContext(), R.color.stryker_accent));
        link.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = dp(6);
        lp.topMargin = dp(24);
        link.setLayoutParams(lp);
        link.setOnClickListener(v -> {
            if (getContext() == null) return;
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
        root.addView(link);
    }

    // ---- helpers --------------------------------------------------------------------------

    private LinearLayout.LayoutParams cardParams(int topMargin) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = topMargin;
        return lp;
    }

    private int primaryTextColor() {
        return resolveThemeColor(android.R.attr.textColorPrimary, 0xFF222222);
    }

    private int secondaryTextColor() {
        return resolveThemeColor(android.R.attr.textColorSecondary, 0xFF757575);
    }

    private int resolveThemeColor(int attr, int fallback) {
        if (getContext() == null) return fallback;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(attr, tv, true)) {
            if (tv.resourceId != 0) {
                return ContextCompat.getColor(getContext(), tv.resourceId);
            }
            return tv.data;
        }
        return fallback;
    }

    private int dp(int value) {
        return Math.round(value * density);
    }
}
