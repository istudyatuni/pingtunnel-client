package com.esrrhs.pingtunnel.client.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.esrrhs.pingtunnel.client.R
import com.esrrhs.pingtunnel.client.pingtunnel.Profile
import com.esrrhs.pingtunnel.client.util.Formatters
import com.esrrhs.pingtunnel.client.vpn.VpnState

// Shadowsocks 风格的状态色
private val SsGreen = Color(0xFF5FB878)
private val SsAmber = Color(0xFFF0A020)
private val SsRed = Color(0xFFE05252)
private val SsGray = Color(0xFF6E7681)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    profiles: List<Profile>,
    selectedProfileId: String?,
    activeId: String?,
    vpnState: VpnState,
    session: SessionTraffic,
    testingPingId: String?,
    qrProfile: Profile?,
    onSelectProfile: (String) -> Unit,
    onConnectionAction: () -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit,
    onAdd: () -> Unit,
    onShowLogs: () -> Unit,
    onSettings: () -> Unit,
    onScan: () -> Unit,
    onPaste: () -> Unit,
    onImport: () -> Unit,
    onExport: () -> Unit,
    onTestPing: (String) -> Unit,
    onShowStats: () -> Unit,
    onShowConnections: () -> Unit,
    onShowIp: () -> Unit,
    onShowHistory: () -> Unit,
    onShowLeak: () -> Unit,
    onShowBrowser: () -> Unit,
    onRunCheck: () -> Unit,
    onDismissCheck: () -> Unit,
    selfCheckState: SelfCheckState,
    onShowQr: (String) -> Unit,
    onDismissQr: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val selectedProfile = profiles.firstOrNull { it.id == selectedProfileId }
    val sessionActive = vpnState is VpnState.Connected ||
        vpnState is VpnState.Connecting ||
        vpnState is VpnState.Disconnecting ||
        vpnState is VpnState.Paused
    val selectedIsActive = selectedProfile != null && selectedProfile.id == activeId
    val selectedVpnState = if (selectedIsActive) vpnState else VpnState.Disconnected
    val connectionActionBlocked = !selectedIsActive &&
        (vpnState is VpnState.Connecting || vpnState is VpnState.Disconnecting)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onShowLeak) {
                        Icon(
                            Icons.Filled.Security,
                            contentDescription = stringResource(R.string.leak_title),
                        )
                    }
                    IconButton(onClick = onSettings) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.action_settings),
                        )
                    }
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = null)
                        }
                        OverflowMenu(
                            expanded = menuExpanded,
                            onDismiss = { menuExpanded = false },
                            onImport = onImport,
                            onExport = onExport,
                            onStats = onShowStats,
                            onConnections = onShowConnections,
                            onIp = onShowIp,
                            onHistory = onShowHistory,
                            onBrowser = onShowBrowser,
                            onLogs = onShowLogs,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            AddProfileFab(
                onAdd = onAdd,
                onPaste = onPaste,
                onScan = onScan,
            )
        },
        bottomBar = {
            ConnectionPanel(
                vpnState = selectedVpnState,
                trafficProfile = selectedProfile,
                activeId = activeId,
                session = session,
                hasSelectedProfile = selectedProfile != null,
                connectionActionBlocked = connectionActionBlocked,
                selfCheckRunning = selectedIsActive && selfCheckState is SelfCheckState.Running,
                onRunCheck = onRunCheck,
                onConnectionAction = onConnectionAction,
            )
        },
    ) { innerPadding ->
        if (profiles.isEmpty()) {
            EmptyProfiles(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(profiles, key = { it.id }) { profile ->
                    ProfileRow(
                        profile = profile,
                        selected = profile.id == selectedProfileId,
                        active = sessionActive && profile.id == activeId,
                        pingTesting = testingPingId == profile.id,
                        onSelect = { onSelectProfile(profile.id) },
                        onEdit = { onEdit(profile.id) },
                        onDelete = { onDelete(profile.id) },
                        onShowQr = { onShowQr(profile.id) },
                        onTestPing = { onTestPing(profile.id) },
                    )
                }
            }
        }
    }

    if (qrProfile != null) {
        QrShareDialog(profile = qrProfile, onDismiss = onDismissQr)
    }
    SelfCheckDialog(state = selfCheckState, onDismiss = onDismissCheck)
}

@Composable
private fun OverflowMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onImport: () -> Unit,
    onExport: () -> Unit,
    onStats: () -> Unit,
    onConnections: () -> Unit,
    onIp: () -> Unit,
    onHistory: () -> Unit,
    onBrowser: () -> Unit,
    onLogs: () -> Unit,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        MenuItem(Icons.Filled.FileDownload, R.string.action_import) { onImport(); onDismiss() }
        MenuItem(Icons.Filled.FileUpload, R.string.action_export) { onExport(); onDismiss() }
        HorizontalDivider()
        MenuItem(Icons.Filled.BarChart, R.string.action_stats) { onStats(); onDismiss() }
        MenuItem(Icons.Filled.Lan, R.string.action_connections) { onConnections(); onDismiss() }
        MenuItem(Icons.Filled.TravelExplore, R.string.action_ip_query) { onIp(); onDismiss() }
        MenuItem(Icons.Filled.History, R.string.action_history) { onHistory(); onDismiss() }
        MenuItem(Icons.Filled.Language, R.string.diag_browser_title) { onBrowser(); onDismiss() }
        MenuItem(Icons.Filled.Description, R.string.menu_logs) { onLogs(); onDismiss() }
    }
}

@Composable
private fun AddProfileFab(
    onAdd: () -> Unit,
    onPaste: () -> Unit,
    onScan: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(
                animationSpec = tween(140),
                expandFrom = Alignment.Bottom,
            ) + scaleIn(
                animationSpec = tween(120),
                initialScale = 0.8f,
                transformOrigin = TransformOrigin(1f, 1f),
            ) + fadeIn(tween(80)),
            exit = shrinkVertically(
                animationSpec = tween(120),
                shrinkTowards = Alignment.Bottom,
            ) + scaleOut(
                animationSpec = tween(100),
                targetScale = 0.8f,
                transformOrigin = TransformOrigin(1f, 1f),
            ) + fadeOut(tween(70)),
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        expanded = false
                        onAdd()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.action_add_profile),
                        )
                    },
                    text = { Text(stringResource(R.string.action_add_profile)) },
                )
                ExtendedFloatingActionButton(
                    onClick = {
                        expanded = false
                        onPaste()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.ContentPaste,
                            contentDescription = stringResource(R.string.action_from_clipboard),
                        )
                    },
                    text = { Text(stringResource(R.string.action_from_clipboard)) },
                )
                ExtendedFloatingActionButton(
                    onClick = {
                        expanded = false
                        onScan()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.QrCodeScanner,
                            contentDescription = stringResource(R.string.action_scan),
                        )
                    },
                    text = { Text(stringResource(R.string.action_scan)) },
                )
            }
        }
        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                if (expanded) Icons.Filled.Close else Icons.Filled.Add,
                contentDescription = stringResource(
                    if (expanded) R.string.action_close else R.string.action_add_profile,
                ),
            )
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    labelRes: Int,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(stringResource(labelRes)) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        enabled = enabled,
        onClick = onClick,
    )
}
@Composable
private fun EmptyProfiles(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.empty_profiles),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ProfileRow(
    profile: Profile,
    selected: Boolean,
    active: Boolean,
    pingTesting: Boolean,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShowQr: () -> Unit,
    onTestPing: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(10.dp)
    val background = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background)
            .then(
                if (selected) Modifier.border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape,
                ) else Modifier,
            )
            .clickable(onClick = onSelect)
            .padding(start = 14.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(
                    if (active) SsGreen else MaterialTheme.colorScheme.outlineVariant,
                ),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = profile.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            val pingText = when {
                pingTesting -> stringResource(R.string.ping_testing)
                profile.pingMs >= 0 -> stringResource(R.string.ping_ms, profile.pingMs)
                else -> stringResource(R.string.ping_untested)
            }
            Text(
                text = "${profile.config.serverHost} · $pingText",
                style = MaterialTheme.typography.bodySmall,
                color = if (pingTesting) SsAmber
                else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Box {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
            ) {
                MenuItem(Icons.Filled.Edit, R.string.action_edit) {
                    menuExpanded = false
                    onEdit()
                }
                MenuItem(Icons.Filled.QrCode, R.string.action_qr) {
                    menuExpanded = false
                    onShowQr()
                }
                MenuItem(
                    icon = Icons.Filled.Speed,
                    labelRes = R.string.action_ping,
                    enabled = !pingTesting,
                ) {
                    menuExpanded = false
                    onTestPing()
                }
                HorizontalDivider()
                MenuItem(Icons.Filled.Delete, R.string.action_delete) {
                    menuExpanded = false
                    onDelete()
                }
            }
        }
    }
}

@Composable
private fun ConnectionPanel(
    vpnState: VpnState,
    trafficProfile: Profile?,
    activeId: String?,
    session: SessionTraffic,
    hasSelectedProfile: Boolean,
    connectionActionBlocked: Boolean,
    selfCheckRunning: Boolean,
    onRunCheck: () -> Unit,
    onConnectionAction: () -> Unit,
) {
    val busy = vpnState is VpnState.Connecting || vpnState is VpnState.Disconnecting
    val canDisconnect = vpnState is VpnState.Connected || vpnState is VpnState.Paused
    val actionLabel = when (vpnState) {
        VpnState.Connecting -> R.string.state_connecting
        VpnState.Disconnecting -> R.string.state_disconnecting
        VpnState.Connected, VpnState.Paused -> R.string.action_disconnect
        else -> R.string.action_connect
    }
    Surface(tonalElevation = 3.dp, shadowElevation = 4.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                StatusLine(vpnState, Modifier.weight(1f))
                if (trafficProfile != null) {
                    val includeSession = trafficProfile.id == activeId
                    val tx = trafficProfile.txBytes + if (includeSession) session.tx else 0L
                    val rx = trafficProfile.rxBytes + if (includeSession) session.rx else 0L
                    Text(
                        text = stringResource(
                            R.string.format_traffic,
                            Formatters.formatBytes(tx),
                            Formatters.formatBytes(rx),
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (vpnState is VpnState.Error && vpnState.message.isNotBlank()) {
                Text(
                    text = vpnState.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = SsRed,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedButton(
                    onClick = onRunCheck,
                    enabled = vpnState is VpnState.Connected && !selfCheckRunning,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                ) {
                    if (selfCheckRunning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = stringResource(R.string.action_test_tunnel),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Button(
                    onClick = onConnectionAction,
                    enabled = !busy && !connectionActionBlocked &&
                        (canDisconnect || hasSelectedProfile),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                ) {
                    if (busy) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = stringResource(actionLabel),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusLine(state: VpnState, modifier: Modifier = Modifier) {
    val (color, labelRes) = when (state) {
        VpnState.Disconnected -> SsGray to R.string.state_disconnected
        VpnState.Connecting -> SsAmber to R.string.state_connecting
        VpnState.Connected -> SsGreen to R.string.state_connected
        VpnState.Disconnecting -> SsAmber to R.string.state_disconnecting
        VpnState.Paused -> SsAmber to R.string.state_paused
        is VpnState.Error -> SsRed to R.string.state_error
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .clip(CircleShape)
                .background(color),
        )
        Spacer(Modifier.width(7.dp))
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
